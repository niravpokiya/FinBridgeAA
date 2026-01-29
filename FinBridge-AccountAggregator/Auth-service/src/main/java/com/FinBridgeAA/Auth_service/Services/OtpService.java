package com.FinBridgeAA.Auth_service.Services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {
    private static final int OTP_LENGTH = 6;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private SecureRandom random = new SecureRandom();

    public String generateOtp() {
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
    /* encoding otp */
    public String encodeOtp(String otp) {
        return passwordEncoder.encode(otp);
    }
    /* matches row OTP with hashed actual otp */
    public boolean verifyOtp(String rawOtp, String hashedOtp) {
        if (hashedOtp == null) {
            return false;
        }
        return passwordEncoder.matches(rawOtp, hashedOtp);
    }
}
