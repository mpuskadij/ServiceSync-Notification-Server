package hr.foi.servicesync.business

import hr.foi.servicesync.data.NotificationData

interface IMessagingProvider {
    fun sendNotification(notificationData: List<NotificationData>)
}