package iuh.fit.favorite_product_service.client;

import iuh.fit.favorite_product_service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProductServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);
    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductServiceClient(RestTemplate restTemplate,
                                @Value("${product.service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public ProductDTO getProductById(Long productId) {
        String url = productServiceUrl + "/api/products/" + productId;
        try {
            return restTemplate.getForObject(url, ProductDTO.class);
        } catch (RestClientException e) {
            logger.error("Error fetching product with ID {}: {}", productId, e.getMessage());
            // Return a placeholder product or throw a more specific exception
            ProductDTO fallbackProduct = new ProductDTO();
            fallbackProduct.setId(productId);
            fallbackProduct.setName("Product information unavailable");
            fallbackProduct.setPrice(0.0);
            return fallbackProduct;
        }
    }
}