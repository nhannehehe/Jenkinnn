package iuh.fit.payment_service.Client;

import iuh.fit.cart_service.DTO.CartItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CartClient {
    private final WebClient webClient;

    public CartClient(WebClient.Builder webClientBuilder, @Value("${cart.service.url}") String cartServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(cartServiceUrl).build();
    }

    // Gọi API lấy giỏ hàng theo username (token phải là của user đó)
    public Mono<List<CartItemDTO>> getCartByUsername(String username, String token) {
        return webClient.get()
                .uri("/api/cart")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(CartItemDTO.class)
                .collectList();
    }

    // Gọi API checkout theo username (token phải là của user đó)
    public Mono<Void> checkoutByUsername(String username, String token) {
        return webClient.post()
                .uri("/api/cart/checkout")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class);
    }
}