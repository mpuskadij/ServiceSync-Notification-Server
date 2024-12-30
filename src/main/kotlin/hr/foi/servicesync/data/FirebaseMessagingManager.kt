package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import hr.foi.servicesync.business.IMessagingProvider
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingManager(private val firebaseMessaging: FirebaseMessaging) : IMessagingProvider {

    override fun sendNotification(notificationData: List<NotificationData>) {
        notificationData.forEach { notification ->
            notification.fcmToken.takeIf { it.isNotEmpty() }?.let { token ->
                val data = mutableMapOf<String, String>().apply {
                    notification.title.takeIf { it.isNotEmpty() }?.let { put("title", it) }
                    notification.body.takeIf { it.isNotEmpty() }?.let { put("body", it) }
                }

                if (data.isNotEmpty()) {
                    try {
                        val message = Message.builder()
                            .setToken(token)
                            .putAllData(data)
                            .build()

                        firebaseMessaging.send(message)
                    } catch (e: Exception) {
                        println("Failed to send notification: ${e.message}")
                    }
                }
            }
        }
    }
}