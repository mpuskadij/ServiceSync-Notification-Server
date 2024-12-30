package hr.foi.servicesync.data

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.QueryDocumentSnapshot

class FirestoreManager {
    private val firestore : Firestore = FirestoreOptions.getDefaultInstance().service
    private val reservationCollectionName = "reservations"
    private val reservationDateField = "reservationDate"
    private val notificationSentField = "notificationSent"

    fun getAllUnsentNotifications(threshold : Number): MutableList<QueryDocumentSnapshot> {
        val unsentNotifications = firestore
            .collection(reservationCollectionName)
            .whereLessThanOrEqualTo(reservationDateField,threshold)
            .whereEqualTo(notificationSentField,false)
            .get()
            .get()
            .documents

        return unsentNotifications
    }
}