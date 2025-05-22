package iuh.fit.user_service.Controller;

import iuh.fit.user_service.DTO.LoginRequest;
import iuh.fit.user_service.DTO.LoginResponse;
import iuh.fit.user_service.DTO.UserDTO;
import iuh.fit.user_service.DTO.UserRegisterRequest;
import iuh.fit.user_service.Security.JwtUtil;
import iuh.fit.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String username = authentication.getName();
        String token = jwtUtil.generateToken(username);
        UserDTO user = userService.findByUsername(username);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(user);
        return response;
    }

    @GetMapping("/userid")
    public Long getUserIdByUsername(@RequestParam String username) {
        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found");
        }
        return user.getId();
    }
}