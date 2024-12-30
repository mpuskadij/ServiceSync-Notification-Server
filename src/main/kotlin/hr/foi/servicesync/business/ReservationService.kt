package hr.foi.servicesync.business
import hr.foi.servicesync.data.NotificationData
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class ReservationService(
    private val reservationDataProvider: IReservationDataProvider,
    private val fcmTokenProvider: IFcmTokenProvider,
    private val messagingProvider: IMessagingProvider
) {

    fun checkForUpcomingReservations() {
        val reservations = mutableListOf<Reservation>()
        val treshold = LocalDateTime.now().plus(Duration.ofHours(24)).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        reservationDataProvider.getAllUnsentNotifications(
            treshold,
            onSuccess = { unsentNotifications ->
                unsentNotifications.forEach {
                    val reservation = it.toObject(Reservation::class.java)
                    reservations.addLast(reservation)
                }
            }
        )
            val allNotifications = mutableListOf<NotificationData>()
            reservations.forEach { reservation ->
                val fcm = fcmTokenProvider.getFcmToken(reservation.userId)
                if (fcm.isNotEmpty()) {
                    val date = LocalDateTime.from(Instant.ofEpochMilli(reservation.reservationDate))
                    val notificationData = NotificationData(
                        fcmToken = fcm,
                        title = "Podsjetnik za rezervaciju",
                        body =  "Vaša rezervacija za ${reservation.serviceName} od tvrtke ${reservation.companyId} počinje ${date}"
                    )
                    allNotifications.addLast(notificationData)
                }
            }

            if (allNotifications.isNotEmpty()) {
                messagingProvider.sendNotification(allNotifications)
            }


    }


}