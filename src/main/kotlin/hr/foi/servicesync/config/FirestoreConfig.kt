package hr.foi.servicesync.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.InputStream


@Configuration
class FirestoreConfig {
    @Bean
    fun firestore() : Firestore {
        val serviceAccount: InputStream = this::class.java.getResourceAsStream("/serviceAccountKey.json")
            ?: throw IllegalStateException("Service account key file not found in classpath")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setProjectId("airtag-servicesync")
            .build()

        FirebaseApp.initializeApp(options)

        return FirestoreClient.getFirestore()
    }

}