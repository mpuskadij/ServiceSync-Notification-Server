package hr.foi.servicesync.config

import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseMessagingConfig {
    @Bean
    fun firebaseMessaging() : FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}