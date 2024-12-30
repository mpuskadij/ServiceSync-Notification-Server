package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import hr.foi.servicesync.business.IMessagingProvider
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingManager(private val firebaseMessaging: FirebaseMessaging) : IMessagingProvider {
    override fun sendNotification(notificationData: List<NotificationData>) {
        notificationData.forEach { notification ->
            if (notification.fcmToken.isNotEmpty()) {
                val data = mapOf(
                    "title" to notification.title,
                    "body" to notification.body
                )
                val message = Message
                    .builder()
                    .setToken(notification.fcmToken)
                    .putAllData(data)
                    .build()

                firebaseMessaging.send(message)
            }

        }

    }
}