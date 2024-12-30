package hr.foi.servicesync.business
import hr.foi.servicesync.data.NotificationData
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class ReservationService(
    private val reservationDataProvider: IReservationDataProvider,
    private val fcmTokenProvider: IFcmTokenProvider,
    private val messagingProvider: IMessagingProvider
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
                    val instant = Instant.ofEpochMilli(reservation.reservationDate)
                    val date = ZonedDateTime.ofInstant(instant,ZoneId.of("UTC")).toLocalDateTime()
                    val notificationData = NotificationData(
                        fcmToken = fcm,
                        title = "Podsjetnik za rezervaciju",
                        body =  "Vaša rezervacija za ${reservation.serviceName} od tvrtke ${reservation.companyId} počinje ${date}"
                    )
                    allNotifications.add(notificationData)
                }
            }

            if (allNotifications.isNotEmpty()) {
                messagingProvider.sendNotification(allNotifications)
            }


    }
}