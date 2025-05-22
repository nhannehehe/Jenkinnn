package iuh.fit.payment_service.service;

import iuh.fit.cart_service.DTO.CartItemDTO;
import iuh.fit.payment_service.Client.CartClient;
import iuh.fit.payment_service.Client.UserClient;
import iuh.fit.payment_service.dto.CheckoutRequest;
import iuh.fit.payment_service.dto.OrderResponse;
import iuh.fit.payment_service.entity.Order;
import iuh.fit.payment_service.entity.OrderStatus;
import iuh.fit.payment_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final EmailService emailService;

    public OrderResponse createOrder(CheckoutRequest request, String username, String token) {
        // 1. Lấy userId từ user service
        Long userId = getUserIdFromUserService(username, token);

        // 2. Tạo đơn hàng mới
        Order order = new Order();
        order.setUserId(userId);
        order.setCustomerName(request.getCustomerInfo().getName());
        order.setCustomerPhone(request.getCustomerInfo().getPhone());
        order.setCustomerEmail(request.getCustomerInfo().getEmail());
        order.setCustomerAddress(request.getCustomerInfo().getAddress());
        order.setTotalAmount(request.getTotalAmount());

        // 3. Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);

        // 4. Xóa giỏ hàng (gọi cart service)
        clearCart(token);

        // 5. Gửi email xác nhận
        emailService.sendOrderConfirmation(savedOrder);

        // 6. Chuyển đổi sang OrderResponse
        return convertToOrderResponse(savedOrder, request.getItems());
    }

    public List<OrderResponse> getOrdersByUserId(Long userId, String token) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(order -> convertToOrderResponse(order, null))
                .collect(Collectors.toList());
    }

    public Long getUserIdFromUserService(String username, String token) {
        try {
            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }
            System.out.println("[PaymentService] Token gửi sang user_service: " + token);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Long> response = restTemplate.exchange(
                "http://localhost:8081/api/auth/userid?username=" + username,
                org.springframework.http.HttpMethod.GET,
                entity,
                Long.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get userId from user service", e);
        }
    }

    private void clearCart(String token) {
        try {
            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            restTemplate.exchange(
                "http://localhost:8083/api/cart/clear",
                org.springframework.http.HttpMethod.DELETE,
                entity,
                Void.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear cart", e);
        }
    }

    private OrderResponse convertToOrderResponse(Order order, List<CheckoutRequest.CartItem> items) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setCustomerAddress(order.getCustomerAddress());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(items);
        return response;
    }

    public void updateOrderStatus(Long orderId, String status) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(OrderStatus.valueOf(status));
            orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }
}