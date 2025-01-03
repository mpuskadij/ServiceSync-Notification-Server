package hr.foi.servicesync.business
import com.google.common.io.Resources
import hr.foi.servicesync.data.NotificationData
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.text.DateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

@Service
class ReservationService(
    private val reservationDataProvider: IReservationDataProvider,
    private val fcmTokenProvider: IFcmTokenProvider,
    private val messagingProvider: IMessagingProvider,
) {
    private val imageName : String = "notification_image.png"

    @Scheduled(fixedRate = 3600000)
    fun checkForUpcomingReservations() {
        println("Checking for upcoming reservations...")
        var reservations = mutableListOf<Reservation>()
        val minThreshold = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        val threshold = LocalDateTime.now().plus(Duration.ofHours(24)).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        reservationDataProvider.getAllUnsentNotifications(
            minThreshold =  minThreshold,
            maxThreshold = threshold,
            onSuccess = { unsentNotifications ->
                reservations = unsentNotifications.toMutableList()
            }
        )
        println("Found ${reservations.size} upcoming reservations that require sending a notification...")
        val notificationMapping = mutableMapOf<NotificationData,Reservation>()
        val allNotifications = mutableListOf<NotificationData>()
            reservations.forEach { reservation ->
                val fcm = fcmTokenProvider.getFcmToken(reservation.userId)
                if (fcm.isNotEmpty()) {
                    val date = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(Date(reservation.reservationDate))
                    val notificationData = NotificationData(
                        fcmToken = fcm,
                        title = "Reservation reminder",
                        body =  "${reservation.serviceName} from ${reservation.companyId} on ${date}"
                    )
                    allNotifications.add(notificationData)
                    notificationMapping[notificationData] = reservation
                }
            }

            if (allNotifications.isNotEmpty()) {
                val pathToImage = Resources.getResource(imageName).path
                messagingProvider.sendNotification(allNotifications,pathToImage,
                    onSuccessfullySentNotifications = {
                        successfullNotifications ->
                        val sentReservations = mutableListOf<Reservation>()
                        successfullNotifications.forEach {
                            val reservation = notificationMapping[it]
                            reservation?.let {
                                sentReservations.add(reservation)
                            }
                        }
                        reservationDataProvider.markNotificationAsSent(sentReservations)

                    }

                )
            }


    }
}