package uz.practise.acu.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudConfig {
    @Bean
    public Storage getStorageService() throws IOException {
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(
                        "C:/Users/HP/Downloads/acuproject-435313-f176782b1965.json")))
                .setProjectId("acuproject-435313")
                .build().getService();
    }
}
