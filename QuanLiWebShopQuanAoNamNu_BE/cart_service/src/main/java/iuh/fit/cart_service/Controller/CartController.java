package iuh.fit.cart_service.Controller;

import iuh.fit.cart_service.DTO.CartItemDTO;
import iuh.fit.cart_service.DTO.CartRequest;
import iuh.fit.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    @Autowired
    private CartService cartService;

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public void addToCart(@RequestBody CartRequest request) {
        String userName = getCurrentUserName();
        if (userName != null) {
            cartService.addToCart(request, userName);
        } else {
            // Nếu không có người dùng đang đăng nhập, trả về lỗi
            throw new RuntimeException("User is not authenticated");
        }
    }

    // Lấy danh sách sản phẩm trong giỏ
    @GetMapping
    public List<CartItemDTO> getCart() {
        String userName = getCurrentUserName();
        if (userName != null) {
            return cartService.getCart(userName);
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    // Thanh toán giỏ hàng
    @PostMapping("/checkout")
    public void checkout() {
        String userName = getCurrentUserName();
        if (userName != null) {
            cartService.checkout(userName);
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    // Xóa toàn bộ giỏ hàng của user hiện tại
    @DeleteMapping("/clear")
    public void clearCart() {
        String userName = getCurrentUserName();
        if (userName != null) {
            cartService.clearCart(userName);
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    // Lấy tên người dùng từ SecurityContext
    private String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
