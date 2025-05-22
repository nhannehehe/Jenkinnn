package iuh.fit.profile_service.service;



import iuh.fit.profile_service.dto.UserProfileDTO;
import iuh.fit.profile_service.entity.UserProfile;
import iuh.fit.profile_service.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfileDTO getProfile(String username) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUsername(username);
        if (userProfileOpt.isEmpty()) {
            throw new RuntimeException("User profile not found");
        }

        UserProfile userProfile = userProfileOpt.get();
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(userProfile.getUsername());
        dto.setEmail(userProfile.getEmail());
        dto.setCreatedAt(userProfile.getCreatedAt());
        return dto;
    }
}