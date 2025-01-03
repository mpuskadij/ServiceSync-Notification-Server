package hr.foi.servicesync.data

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import hr.foi.servicesync.business.IFcmTokenProvider
import hr.foi.servicesync.business.IReservationDataProvider
import hr.foi.servicesync.business.Reservation
import org.springframework.stereotype.Repository

@Repository
class FirestoreManager(private val firestore: Firestore) : IReservationDataProvider,IFcmTokenProvider {
    private val reservationCollectionName = "reservations"
    private val reservationDateField = "reservationDate"
    private val userCollectionName = "users"
    private val fcmTokenField = "FCMToken"
    private val notificationSentField = "notificationSent"
    private val reservationDocumentMap = mutableMapOf<Reservation,QueryDocumentSnapshot>()

    override fun getAllUnsentNotifications(minThreshold : Long, maxThreshold: Long, onSuccess: (List<Reservation>) -> Unit) {
        try {
            val unsentNotifications = firestore
                .collection(reservationCollectionName)
                .whereGreaterThan(reservationDateField,minThreshold)
                .whereLessThanOrEqualTo(reservationDateField,maxThreshold)
                .whereEqualTo(notificationSentField,false)
                .get()
                .get()
                .documents

            if (unsentNotifications.isNotEmpty()) {
                val reservations = mutableListOf<Reservation>()
                unsentNotifications.forEach { unsentNotification ->
                    val reservation = unsentNotification.toObject(Reservation::class.java)
                    reservations.add(reservation)
                    reservationDocumentMap[reservation] = unsentNotification
                }
                onSuccess(reservations)
            }
        }
        catch (e : Exception) {
            println("Error while fetching unsent notifications: ${e.message}")
        }

    }

    override fun markNotificationAsSent(successfullySentReservationNotifications: List<Reservation>) {
        try {
            if (successfullySentReservationNotifications.isNotEmpty()) {
                successfullySentReservationNotifications.forEach {
                    reservationDocumentMap[it]?.reference?.update(notificationSentField,true)
                }
            }
            else {
                throw Exception("Received 0 reservations to update!")
            }
        }
        catch (e : Exception) {
            println("Error updating notificationSentField. Error: ${e.message}")
        }
    }

    override fun getFcmToken(username: String): String {
         try {
            return firestore
                .collection(userCollectionName)
                .document(username)
                .get()
                .get()
                .getString(fcmTokenField) ?: ""
        }
        catch (e : Exception) {
            println("Error fetching fcm token of $username. Error: ${e.message}")
            return ""
        }

    }
}