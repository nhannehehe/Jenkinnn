package iuh.fit.profile_service.controller;


import iuh.fit.profile_service.dto.UserProfileDTO;
import iuh.fit.profile_service.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public UserProfileDTO getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileService.getProfile(username);
    }
}