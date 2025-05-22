package iuh.fit.payment_service.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {
    private final WebClient webClient;

    public UserClient(@Value("${user.service.url:http://localhost:8081}") String userServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(userServiceUrl).build();
    }

    public Mono<Long> getUserIdByUsername(String username, String token) {
        return webClient.get()
                .uri("/api/auth/userid?username=" + username)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Long.class);
    }
} 