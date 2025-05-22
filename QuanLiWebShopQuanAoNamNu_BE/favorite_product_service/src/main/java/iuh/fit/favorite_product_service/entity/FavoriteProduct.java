package iuh.fit.favorite_product_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;
}
