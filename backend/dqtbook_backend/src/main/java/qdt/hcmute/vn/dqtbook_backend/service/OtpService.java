package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, OtpEntry> otpStorage = new HashMap<>();
    private final Random random = new Random();

    private static final long EXPIRATION_TIME = 5 * 60; // 5 phút

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000)); // 6 chữ số
        otpStorage.put(email, new OtpEntry(otp, Instant.now().plusSeconds(EXPIRATION_TIME)));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpEntry entry = otpStorage.get(email);
        if (entry == null) return false;
        if (Instant.now().isAfter(entry.expiry)) {
            otpStorage.remove(email);
            return false;
        }
        boolean isValid = entry.otp.equals(otp);
        if (isValid) otpStorage.remove(email); // Xóa sau khi dùng
        return isValid;
    }

    private static class OtpEntry {
        String otp;
        Instant expiry;
        OtpEntry(String otp, Instant expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }
}
