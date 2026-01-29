package com.FinBridgeAA.user_service.Service;

import com.FinBridgeAA.user_service.DTO.UserProfileResponseDto;
import com.FinBridgeAA.user_service.Entity.UserProfile;
import com.FinBridgeAA.user_service.Enums.KycStatus;
import com.FinBridgeAA.user_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void createUserIfNotExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            UserProfile user = new UserProfile();
            user.setUserId(userId);
            user.setKycStatus(KycStatus.NOT_STARTED);
            userRepository.save(user);
        }
    }

    public UserProfileResponseDto getProfile(UUID userId) {
        System.out.println("Fetching profile for userId: " + userId);
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDob(user.getDob());
        dto.setKycStatus(user.getKycStatus());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public UserProfileResponseDto updateProfile(UUID userId, UserProfileResponseDto dto) {
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setDob(dto.getDob());

        UserProfile saved = userRepository.save(user);

        return modelMapper.map(saved, UserProfileResponseDto.class);
    }

    public KycStatus getKycStatus(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getKycStatus();
    }
}