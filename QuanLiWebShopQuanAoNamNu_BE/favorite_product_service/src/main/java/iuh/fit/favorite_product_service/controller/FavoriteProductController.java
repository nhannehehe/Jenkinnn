package iuh.fit.favorite_product_service.controller;

import iuh.fit.favorite_product_service.dto.FavoriteProductDTO;
import iuh.fit.favorite_product_service.entity.FavoriteProduct;
import iuh.fit.favorite_product_service.service.FavoriteProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteProductController {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteProductController.class);
    private final FavoriteProductService service;

    @PostMapping
    public ResponseEntity<?> addFavoriteAndReturnList(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        try {
            service.addFavorite(userId, productId);
            List<FavoriteProductDTO> favorites = service.getFavoritesByUser(userId);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            logger.error("Error adding favorite product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add favorite product: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getFavorites(@PathVariable Long userId) {
        try {
            List<FavoriteProductDTO> favoriteProducts = service.getFavoritesByUser(userId);
            return ResponseEntity.ok(favoriteProducts);
        } catch (Exception e) {
            logger.error("Error getting favorite products for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve favorite products: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            service.removeFavorite(userId, productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error removing favorite product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove favorite product: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> isFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            return ResponseEntity.ok(service.isFavorite(userId, productId));
        } catch (Exception e) {
            logger.error("Error checking favorite status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to check favorite status: " + e.getMessage());
        }
    }
}