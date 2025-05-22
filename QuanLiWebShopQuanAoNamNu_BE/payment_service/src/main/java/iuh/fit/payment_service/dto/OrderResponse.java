package iuh.fit.payment_service.dto;

import iuh.fit.payment_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<CheckoutRequest.CartItem> items;
    private String vnpTransactionNo;
    private String vnpResponseCode;
} 