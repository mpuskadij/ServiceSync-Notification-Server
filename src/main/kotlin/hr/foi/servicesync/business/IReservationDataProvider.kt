package hr.foi.servicesync.business

import com.google.cloud.firestore.QueryDocumentSnapshot

interface IReservationDataProvider {
    fun getAllUnsentNotifications(
        threshold: Long,
        onSuccess: (MutableList<QueryDocumentSnapshot>) -> Unit
    )

    fun markNotificationAsSent(reservationDocumentSnapshot: QueryDocumentSnapshot)
}