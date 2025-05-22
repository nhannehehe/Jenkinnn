package iuh.fit.favorite_product_service.service;

import com.example.productservice.dto.ProductDTO;
import iuh.fit.favorite_product_service.client.ProductServiceClient;
import iuh.fit.favorite_product_service.dto.FavoriteProductDTO;
import iuh.fit.favorite_product_service.entity.FavoriteProduct;
import iuh.fit.favorite_product_service.repository.FavoriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteProductService {

    private final FavoriteProductRepository repository;
    private final ProductServiceClient productServiceClient;

    public FavoriteProduct addFavorite(Long userId, Long productId) {
        List<FavoriteProduct> existingFavorites = repository.findByUserIdAndProductId(userId, productId);
        if (!existingFavorites.isEmpty()) {
            // Đã có ít nhất 1 bản ghi trùng, không thêm nữa
            return null;
        }
        return repository.save(FavoriteProduct.builder()
                .userId(userId)
                .productId(productId)
                .build());
    }

    public List<FavoriteProductDTO> getFavoritesByUser(Long userId) {
        List<FavoriteProduct> favoriteProducts = repository.findByUserId(userId);
        return favoriteProducts.stream()
                .map(fav -> {
                    ProductDTO product = productServiceClient.getProductById(fav.getProductId());
                    return new FavoriteProductDTO(fav.getId(), product);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        repository.deleteByUserIdAndProductId(userId, productId);
    }

    public boolean isFavorite(Long userId, Long productId) {
        // repository.findByUserIdAndProductId(userId, productId).isPresent(); // sai, trả về List chứ không phải Optional
        List<FavoriteProduct> favorites = repository.findByUserIdAndProductId(userId, productId);
        return !favorites.isEmpty();
    }

}