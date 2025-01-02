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
    private val imageName : String = "notification_image.png"
) {

    @Scheduled(fixedRate = 360000)
    fun checkForUpcomingReservations() {
        val reservations = mutableListOf<Reservation>()
        val treshold = LocalDateTime.now().plus(Duration.ofHours(24)).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        reservationDataProvider.getAllUnsentNotifications(
            treshold,
            onSuccess = { unsentNotifications ->
                unsentNotifications.forEach {
                    val reservation = it.toObject(Reservation::class.java)
                    reservations.add(reservation)
                }
            }
        )
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
                }
            }

            if (allNotifications.isNotEmpty()) {
                val pathToImage = Resources.getResource(imageName).path
                messagingProvider.sendNotification(allNotifications,pathToImage)
                //TODO add marking reservation as sent
            }


    }
}