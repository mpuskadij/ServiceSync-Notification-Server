package hr.foi.servicesync.business


interface IReservationDataProvider {
    fun getAllUnsentNotifications(
        minThreshold : Long,
        maxThreshold: Long,
        onSuccess: (List<Reservation>) -> Unit
    )

    fun markNotificationAsSent(successfullySentReservationNotifications: List<Reservation>)
}