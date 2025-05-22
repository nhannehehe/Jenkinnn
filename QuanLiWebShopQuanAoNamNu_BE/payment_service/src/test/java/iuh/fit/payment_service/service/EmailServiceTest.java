package iuh.fit.payment_service.service;

import iuh.fit.payment_service.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Test User");
        order.setCustomerEmail("your-test-email@gmail.com"); // Thay bằng email test của bạn
        order.setTotalAmount(new BigDecimal("1000000"));
        order.setCustomerAddress("123 Test Street");

        emailService.sendOrderConfirmation(order);
    }
} 