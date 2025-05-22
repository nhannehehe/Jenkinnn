package iuh.fit.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private CustomerInfo customerInfo;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private String orderId;
    private Long amount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private String name;
        private String phone;
        private String email;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private Long productId;
        private Integer quantity;
        private BigDecimal price;
    }
} 