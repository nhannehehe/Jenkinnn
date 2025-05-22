package iuh.fit.cart_service.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_Name")
    private String userName;
    @Column(name = "product_id")
    private Long productId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
