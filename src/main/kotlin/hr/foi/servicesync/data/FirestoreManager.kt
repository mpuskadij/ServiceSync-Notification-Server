package hr.foi.servicesync.data

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import org.springframework.stereotype.Repository

@Repository
class FirestoreManager(private val firestore: Firestore) {
    private val reservationCollectionName = "reservations"
    private val reservationDateField = "reservationDate"
    private val notificationSentField = "notificationSent"

    fun getAllUnsentNotifications(threshold : Number, onSuccess: (MutableList<QueryDocumentSnapshot>) -> Unit) {
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

    fun markNotificationAsSent(reservationDocumentSnapshot: QueryDocumentSnapshot) {
        reservationDocumentSnapshot.reference.update(notificationSentField,true)
    }
}