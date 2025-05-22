package iuh.fit.payment_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String status;
    private String transactionId;
    private LocalDateTime createdAt;
}
