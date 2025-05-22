package iuh.fit.payment_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VnpayConfig {
    @Value("${vnpay.terminal-id}")
    private String terminalId;

    @Value("${vnpay.secret-key}")
    private String secretKey;

    @Value("${vnpay.payment-url}")
    private String paymentUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    // Getters
    public String getTerminalId() {
        return terminalId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }
} 