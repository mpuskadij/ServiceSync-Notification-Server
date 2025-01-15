package hr.foi.servicesync.config

import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PortListener {
    @EventListener
    fun onApplicationStart(event: WebServerInitializedEvent) {
        val port = event.webServer.port
        println("ServiceSync Notification Server is running on port: ${port}")
    }
}