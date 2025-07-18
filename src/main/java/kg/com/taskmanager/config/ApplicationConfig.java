package kg.com.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;

@Configuration
@EnableAsync
public class ApplicationConfig {

    @Bean
    public RestClient restClientBuilderBuilder() {
        return RestClient.builder()
                .baseUrl("https://api.restful-api.dev")
                .build();
    }
}