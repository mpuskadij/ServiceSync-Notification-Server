package hr.foi.servicesync.business

data class Reservation(
    val companyId : String,
    val serviceName: String,
    val reservationDate: Long,
    val notificationSent: Boolean,
    val userId : String
)
