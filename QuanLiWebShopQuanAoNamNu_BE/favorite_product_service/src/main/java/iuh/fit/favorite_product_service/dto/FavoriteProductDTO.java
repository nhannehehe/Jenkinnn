package iuh.fit.favorite_product_service.dto;

import iuh.fit.favorite_product_service.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteProductDTO {
    private Long id;
    private ProductDTO product;
}
