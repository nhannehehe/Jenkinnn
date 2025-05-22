package iuh.fit.user_service.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserDTO user;
}