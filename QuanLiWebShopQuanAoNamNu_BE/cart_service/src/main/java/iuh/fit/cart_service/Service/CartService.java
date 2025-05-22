package iuh.fit.cart_service.Service;

import iuh.fit.cart_service.DTO.CartItemDTO;
import iuh.fit.cart_service.DTO.CartRequest;
import iuh.fit.cart_service.DTO.ProductDTO;
import iuh.fit.cart_service.Entity.Cart;
import iuh.fit.cart_service.Repository.CartRepository;
import iuh.fit.cart_service.client.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductClient productClient;

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(CartRequest request, String userName) {
        // Kiểm tra xem sản phẩm đã có trong giỏ của người dùng chưa
        Optional<Cart> existingCart = cartRepository.findByUserNameAndProductId(userName, request.getProductId());
        if (existingCart.isPresent()) {
            // Nếu có, cập nhật số lượng sản phẩm
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
            cartRepository.save(cart);
        } else {
            // Nếu không, tạo mới sản phẩm trong giỏ
            Cart cart = new Cart();
            cart.setProductId(request.getProductId());
            cart.setUserName(userName);
            cart.setQuantity(request.getQuantity());
            cartRepository.save(cart);
        }
    }

    // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
    public List<CartItemDTO> getCart(String userName) {
        List<Cart> cartItems = cartRepository.findByUserName(userName);
        return cartItems.stream()
                .map(cart -> {
                    // Gọi Product Service để lấy thông tin sản phẩm
                    Mono<ProductDTO> productMono = productClient.getProductById(cart.getProductId());

                    // Sử dụng block() nếu bạn cần kết quả đồng bộ, nếu không có thể sử dụng phản hồi bất đồng bộ
                    ProductDTO product = productMono.block(); // Chú ý xử lý ngoại lệ khi sản phẩm không tồn tại

                    if (product == null) {
                        // Nếu sản phẩm không tìm thấy, bạn có thể ném exception hoặc trả về sản phẩm mặc định
                        return null;  // hoặc ném exception tùy vào cách bạn muốn xử lý
                    }

                    CartItemDTO cartItemDTO = new CartItemDTO();
                    cartItemDTO.setId(cart.getId());
                    cartItemDTO.setProductId(cart.getProductId());
                    cartItemDTO.setProductName(product.getName());
                    cartItemDTO.setProductPrice(product.getPrice());
                    cartItemDTO.setProductImage(product.getImage());
                    cartItemDTO.setQuantity(cart.getQuantity());
                    return cartItemDTO;
                })
                .filter(cartItemDTO -> cartItemDTO != null) // Loại bỏ các sản phẩm không hợp lệ
                .collect(Collectors.toList());
    }

    // Thanh toán và xóa giỏ hàng
    @Transactional
    public void checkout(String userName) {
        cartRepository.deleteByUserName(userName);
    }

    @Transactional
    public void clearCart(String userName) {
        cartRepository.deleteByUserName(userName);
    }
}
