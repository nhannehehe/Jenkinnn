package iuh.fit.payment_service.service;

import iuh.fit.payment_service.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getCustomerEmail());
        message.setSubject("Xác nhận đơn hàng #" + order.getId());
        message.setText(String.format(
            "Kính gửi %s,\n\n" +
            "Cảm ơn bạn đã đặt hàng tại shop của chúng tôi.\n\n" +
            "Thông tin đơn hàng:\n" +
            "Mã đơn hàng: %d\n" +
            "Tổng tiền: %s\n" +
            "Địa chỉ giao hàng: %s\n\n" +
            "Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất.\n\n" +
            "Trân trọng,\n" +
            "Shop Quần Áo",
            order.getCustomerName(),
            order.getId(),
            order.getTotalAmount(),
            order.getCustomerAddress()
        ));

        mailSender.send(message);
    }
} 