package hr.foi.servicesync.business

import com.google.cloud.firestore.QueryDocumentSnapshot

interface IReservationDataProvider {
    fun getAllUnsentNotifications(
        threshold: Number,
        onSuccess: (MutableList<QueryDocumentSnapshot>) -> Unit
    )

    fun markNotificationAsSent(reservationDocumentSnapshot: QueryDocumentSnapshot)
}