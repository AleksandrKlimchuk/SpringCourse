package service2ui;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateFactory {
    @Bean
    public RestTemplate create() {
        return new RestTemplateBuilder().build();
    }
}
