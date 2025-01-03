package hr.foi.servicesync.data

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import hr.foi.servicesync.business.IMessagingProvider
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingManager(private val firebaseMessaging: FirebaseMessaging) : IMessagingProvider {

    override fun sendNotification(notificationData: List<NotificationData>, imageURL: String,onSuccessfullySentNotifications: (List<NotificationData>) -> Unit) {
        val messages = mutableListOf<Message>()
        val notificationMapping = mutableMapOf<Message,NotificationData>()
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
                    notificationMapping[message] = notification
            }

        }
        if (messages.isNotEmpty()) {
            val successfulNotifications = mutableListOf<NotificationData>()
            val batchResponse = firebaseMessaging.sendEach(messages)
            println("Number of successfully sent notifications: ${batchResponse.successCount}")
            println("Number of failed notifications: ${batchResponse.failureCount}")
            val successfulResponses = batchResponse.responses.filter { it.exception == null }
            successfulResponses.forEachIndexed { index, sendResponse ->
                val message = messages[index]
                val notification = notificationMapping[message]
                notification?.let {
                    successfulNotifications.add(it)
                }
            }
            val failedResponses = batchResponse.responses.filter { it.exception != null }
            failedResponses.forEach {
                println(it.exception.cause)
            }
            if (successfulNotifications.isNotEmpty()) {
                onSuccessfullySentNotifications(successfulNotifications)
            }
        }
    }
}