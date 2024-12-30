package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

class FirebaseMessagingManager {
    fun sendNotification(fcmToken: String, notificationData: NotificationData) {
        val data = mapOf(
            "title" to notificationData.title,
            "body" to notificationData.body
        )
        val message = Message
            .builder()
            .setToken(fcmToken)
            .putAllData(data)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }
}