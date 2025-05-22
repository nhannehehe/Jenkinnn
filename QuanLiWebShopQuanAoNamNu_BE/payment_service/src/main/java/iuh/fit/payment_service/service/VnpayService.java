package iuh.fit.payment_service.service;

import iuh.fit.payment_service.config.VnpayConfig;
import iuh.fit.payment_service.dto.CheckoutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnpayService {
    @Autowired
    private VnpayConfig vnpayConfig;

    public String createVnpayPaymentUrl(CheckoutRequest request, String username) {
        try {
            // Tạo các tham số cần thiết cho VNPAY
            Map<String, String> vnpayParams = new HashMap<>();
            vnpayParams.put("vnp_Version", "2.1.0");
            vnpayParams.put("vnp_Command", "pay");
            vnpayParams.put("vnp_TmnCode", vnpayConfig.getTerminalId());
            vnpayParams.put("vnp_Amount", String.valueOf(request.getAmount() * 100)); // Số tiền * 100 (VNPAY yêu cầu)
            vnpayParams.put("vnp_CurrCode", "VND");
            vnpayParams.put("vnp_TxnRef", UUID.randomUUID().toString()); // Mã giao dịch ngẫu nhiên
            vnpayParams.put("vnp_OrderInfo", "Thanh toan don hang: " + request.getOrderId());
            vnpayParams.put("vnp_OrderType", "billpayment");
            vnpayParams.put("vnp_Locale", "vn");
            vnpayParams.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
            vnpayParams.put("vnp_IpAddr", "127.0.0.1"); // IP của người dùng (cần thay đổi khi triển khai)
            vnpayParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            // Sắp xếp các tham số theo thứ tự a-z
            List<String> paramNames = new ArrayList<>(vnpayParams.keySet());
            Collections.sort(paramNames);

            // Tạo chuỗi hash data và query string
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (int i = 0; i < paramNames.size(); i++) {
                String paramName = paramNames.get(i);
                String paramValue = vnpayParams.get(paramName);
                if (paramValue != null && !paramValue.isEmpty()) {
                    hashData.append(paramName).append("=").append(URLEncoder.encode(paramValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(paramName, StandardCharsets.US_ASCII.toString()))
                         .append("=")
                         .append(URLEncoder.encode(paramValue, StandardCharsets.US_ASCII.toString()));
                    if (i < paramNames.size() - 1) {
                        hashData.append("&");
                        query.append("&");
                    }
                }
            }

            // Tạo chuỗi hash
            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

            // Tạo URL thanh toán VNPAY
            return vnpayConfig.getPaymentUrl() + "?" + queryUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Hàm tạo chuỗi hash HMAC-SHA512
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmacSha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmacSha512.init(secretKey);
            byte[] hmacData = hmacSha512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hmacData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 