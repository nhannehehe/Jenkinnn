package iuh.fit.cart_service.DTO;

import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private Integer quantity;
}
