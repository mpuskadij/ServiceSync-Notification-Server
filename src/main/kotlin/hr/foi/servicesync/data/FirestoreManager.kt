package hr.foi.servicesync.data

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import hr.foi.servicesync.business.IFcmTokenProvider
import hr.foi.servicesync.business.IReservationDataProvider
import org.springframework.stereotype.Repository

@Repository
class FirestoreManager(private val firestore: Firestore) : IReservationDataProvider,IFcmTokenProvider {
    private val reservationCollectionName = "reservations"
    private val reservationDateField = "reservationDate"
    private val userCollectionName = "users"
    private val fcmTokenField = "FCMToken"
    private val notificationSentField = "notificationSent"

    override fun getAllUnsentNotifications(threshold : Long, onSuccess: (MutableList<QueryDocumentSnapshot>) -> Unit) {
        try {
            val unsentNotifications = firestore
                .collection(reservationCollectionName)
                .whereLessThanOrEqualTo(reservationDateField,threshold)
                .whereEqualTo(notificationSentField,false)
                .get()
                .get()
                .documents

            if (unsentNotifications.isNotEmpty()) {
                onSuccess(unsentNotifications)
            }
        }
        catch (e : Exception) {
            println("Error while fetching unsent notifications: ${e.message}")
        }

    }

    override fun markNotificationAsSent(reservationDocumentSnapshot: QueryDocumentSnapshot) {
        try {
            reservationDocumentSnapshot.reference.update(notificationSentField,true)
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