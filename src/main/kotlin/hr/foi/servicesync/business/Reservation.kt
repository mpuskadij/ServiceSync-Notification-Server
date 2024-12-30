package hr.foi.servicesync.business

data class Reservation(
    val companyId : String = "",
    val serviceName: String = "",
    val reservationDate: Long = 0,
    val notificationSent: Boolean = false,
    val userId : String = ""
)
