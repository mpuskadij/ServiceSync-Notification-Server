package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import hr.foi.servicesync.business.IMessagingProvider
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingManager(private val firebaseMessaging: FirebaseMessaging) : IMessagingProvider {

    override fun sendNotification(notificationData: List<NotificationData>) {
        val messages = mutableListOf<Message>()
        notificationData.forEach { notification ->
            if (notification.fcmToken.isNotEmpty() && notification.body.isNotEmpty() && notification.title.isNotEmpty()) {
                val data = mutableMapOf<String, String>().apply {
                    "title" to notification.title
                    "body" to notification.body
                }

                if (data.isNotEmpty()) {
                    val message = Message.builder()
                        .setToken(notification.fcmToken)
                        .putAllData(data)
                        .build()

                    messages.add(message)
                }
            }

        }
        if (messages.isNotEmpty()) {
            firebaseMessaging.sendEach(messages)
        }
    }
}