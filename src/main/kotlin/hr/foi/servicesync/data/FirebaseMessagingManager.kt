package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import hr.foi.servicesync.business.IMessagingProvider
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingManager(private val firebaseMessaging: FirebaseMessaging) : IMessagingProvider {
    override fun sendNotification(fcmToken: String, notificationData: NotificationData) {
        val data = mapOf(
            "title" to notificationData.title,
            "body" to notificationData.body
        )
        val message = Message
            .builder()
            .setToken(fcmToken)
            .putAllData(data)
            .build()

        firebaseMessaging.send(message)
    }
}