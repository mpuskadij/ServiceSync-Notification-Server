package hr.foi.servicesync.config

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirestoreConfig {
    @Bean
    fun firestore() : Firestore {
        return FirestoreOptions.getDefaultInstance().service
    }

}