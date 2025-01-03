package hr.foi.servicesync.data

data class NotificationData(
    val fcmToken: String,
    val title: String,
    val body: String
)