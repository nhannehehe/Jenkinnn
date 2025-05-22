package iuh.fit.user_service.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String gender;
}