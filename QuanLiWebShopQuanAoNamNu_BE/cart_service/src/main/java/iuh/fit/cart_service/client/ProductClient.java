package iuh.fit.cart_service.client;

import iuh.fit.cart_service.DTO.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {
    private final WebClient webClient;

    public ProductClient(WebClient.Builder webClientBuilder, @Value("${product.service.url}") String productServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(productServiceUrl).build();
    }

    public Mono<ProductDTO> getProductById(Long productId) {
        return webClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }
}
