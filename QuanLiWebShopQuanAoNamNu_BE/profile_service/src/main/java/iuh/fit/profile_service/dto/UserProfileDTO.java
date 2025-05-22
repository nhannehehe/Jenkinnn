package iuh.fit.profile_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserProfileDTO {
    private String username;
    private String email;
    private LocalDateTime createdAt;
}