package iuh.fit.cart_service.DTO;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id; // ID của mục trong giỏ hàng
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImage;
    private Integer quantity;
    private Double totalPrice;

    public void calculateTotalPrice() {
        this.totalPrice = this.productPrice * this.quantity;
    }
}