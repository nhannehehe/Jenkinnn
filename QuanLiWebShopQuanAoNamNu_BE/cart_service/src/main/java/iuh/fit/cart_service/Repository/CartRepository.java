package iuh.fit.cart_service.Repository;

import iuh.fit.cart_service.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserName(String userName);
    void deleteByUserName(String userName);
    Optional<Cart> findByUserNameAndProductId(String userName, Long productId);
}