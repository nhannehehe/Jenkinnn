package iuh.fit.payment_service.controller;

import iuh.fit.payment_service.dto.CheckoutRequest;
import iuh.fit.payment_service.dto.OrderResponse;
import iuh.fit.payment_service.service.PaymentService;
import iuh.fit.payment_service.security.JwtUtil;
import iuh.fit.payment_service.service.VnpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private VnpayService vnpayService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestBody CheckoutRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            // Xử lý token để lấy username
            String username = extractUsernameFromToken(token);
            
            // Tạo đơn hàng
            OrderResponse order = paymentService.createOrder(request, username, token);
            
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi ra console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing checkout: " + e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestHeader("Authorization") String token) {
        try {
            String username = extractUsernameFromToken(token);
            Long userId = paymentService.getUserIdFromUserService(username, token);
            List<OrderResponse> orders = paymentService.getOrdersByUserId(userId, token);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi ra console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting orders: " + e.getMessage());
        }
    }

    @PostMapping("/vnpay-checkout")
    public ResponseEntity<?> vnpayCheckout(
            @RequestBody CheckoutRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            String username = extractUsernameFromToken(token);
            String paymentUrl = vnpayService.createVnpayPaymentUrl(request, username);
            if (paymentUrl != null) {
                return ResponseEntity.ok(Collections.singletonMap("paymentUrl", paymentUrl));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error creating VNPAY payment URL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing VNPAY checkout: " + e.getMessage());
        }
    }

    @GetMapping("/vnpay-callback")
    public ResponseEntity<?> vnpayCallback(@RequestParam Map<String, String> params) {
        try {
            // Xử lý kết quả thanh toán từ VNPAY
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            String vnp_TransactionNo = params.get("vnp_TransactionNo");
            String vnp_OrderInfo = params.get("vnp_OrderInfo");
            String vnp_Amount = params.get("vnp_Amount");

            // Lấy orderId từ vnp_OrderInfo (ví dụ: "Thanh toan don hang: 123456" -> "123456")
            String orderId = vnp_OrderInfo.split(": ")[1];

            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công
                paymentService.updateOrderStatus(Long.parseLong(orderId), "PAID");
                return ResponseEntity.ok("Payment successful");
            } else {
                // Thanh toán thất bại
                paymentService.updateOrderStatus(Long.parseLong(orderId), "FAILED");
                return ResponseEntity.ok("Payment failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing VNPAY callback: " + e.getMessage());
        }
    }

    private String extractUsernameFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUsername(token);
    }
}
