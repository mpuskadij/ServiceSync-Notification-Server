package hr.foi.servicesync.business

import com.google.cloud.firestore.QueryDocumentSnapshot

interface IReservationDataProvider {
    fun getAllUnsentNotifications(
        minThreshold : Long,
        maxThreshold: Long,
        onSuccess: (MutableList<QueryDocumentSnapshot>) -> Unit
    )

    fun markNotificationAsSent(reservationDocumentSnapshot: QueryDocumentSnapshot)
}