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

    override fun markNotificationAsSent(reservationDocumentSnapshot: QueryDocumentSnapshot) {
        reservationDocumentSnapshot.reference.update(notificationSentField,true)
    }

    override fun getFcmToken(username: String): String {
        return firestore
            .collection(userCollectionName)
            .document(username)
            .get()
            .get()
            .getString(fcmTokenField) ?: ""

    }
}