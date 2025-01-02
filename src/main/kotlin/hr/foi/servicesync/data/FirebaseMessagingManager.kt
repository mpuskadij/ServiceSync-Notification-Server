package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import hr.foi.servicesync.business.IMessagingProvider
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingManager(private val firebaseMessaging: FirebaseMessaging) : IMessagingProvider {

    override fun sendNotification(notificationData: List<NotificationData>, imageURL: String) {
        val messages = mutableListOf<Message>()
        notificationData.forEach { notification ->
            if (notification.fcmToken.isNotEmpty() && notification.body.isNotEmpty() && notification.title.isNotEmpty()) {
                val data = hashMapOf(
                    "title" to notification.title,
                    "body" to notification.body
                )


                    val message = Message.builder()
                        .setToken(notification.fcmToken)
                        .setNotification(
                            Notification
                                .builder()
                                .setTitle(data["title"])
                                .setBody(data["body"])
                                .setImage(imageURL)
                                .build()
                        )
                        .build()

                    messages.add(message)
            }

        }
        if (messages.isNotEmpty()) {
            val batchResponse = firebaseMessaging.sendEach(messages)
            println("Number of successfully sent notifications: ${batchResponse.successCount}")
            println("Number of failed notifications: ${batchResponse.failureCount}")
            val failedResponses = batchResponse.responses.filter { it.exception != null }
            failedResponses.forEach {
                println(it.exception.cause)
            }

        }
    }
}