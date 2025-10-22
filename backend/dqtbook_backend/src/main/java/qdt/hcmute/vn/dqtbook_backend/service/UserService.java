package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.model.Department;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.DepartmentRepository;
import qdt.hcmute.vn.dqtbook_backend.dto.UserCreateRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.UserUpdateRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.UserResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession session;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private FileStorageService fileStorageService;

    public UserService(UserRepository userRepository,
            DepartmentRepository departmentRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    public Optional<UserResponseDTO> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return Optional.ofNullable(user).map(this::convertToResponseDTO);
    }

    @Transactional
    public boolean sendOtpForRegistration(String email) {
        // Kiểm tra email có tồn tại chưa
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Kiểm tra email có thuộc domain của trường không (@student.hcmute.edu.vn hoặc @hcmute.edu.vn)
        if (!email.endsWith("@student.hcmute.edu.vn") && !email.endsWith("@hcmute.edu.vn")) {
            throw new IllegalArgumentException("Email must belong to hcmute.edu.vn domain");
        }

        // Sinh OTP và gửi mail
        String otp = otpService.generateOtp(email);
        emailService.sendSimpleEmail(
            email,
            "Email Verification Code",
            "Your verification code is: " + otp + "\n\nThis code will expire in 5 minutes."
        );

        return true;
    }

    @Transactional
    public Optional<UserResponseDTO> verifyOtpAndCreateUser(UserCreateRequestDTO dto) {
        String otp = dto.getOtp();
        boolean valid = otpService.verifyOtp(dto.getEmail(), otp);
        if (!valid) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        // Sau khi OTP hợp lệ → tạo user như trước
        User user = new User();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAvatarUrl(dto.getAvatarUrl() != null ? dto.getAvatarUrl() : "http://localhost:8080/default-avatar.png");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        if (dto.getDepartmentId() != null) {
            departmentRepository.findById(dto.getDepartmentId()).ifPresent(user::setDepartment);
        }

        User savedUser = userRepository.save(user);

        emailService.sendSimpleEmail(
            user.getEmail(),
            "Welcome to Our Platform",
            "Dear " + user.getFullName() + ",\n\nYour email has been verified successfully!\nYou are now a part of our comunity. Enjoy!\n\nBest regards,\nThe QDT Team"
        );

        return Optional.of(convertToResponseDTO(savedUser));
    }

    @Transactional
    public boolean sendOtpForForgotPassword(String email) {
        // Kiểm tra email có tồn tại không
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email does not exist");
        }

        // Sinh OTP và gửi mail
        String otp = otpService.generateOtp(email);
        emailService.sendSimpleEmail(
            email,
            "Email Verification Code for Password Reset",
            "Your verification code is: " + otp + "\n\nThis code will expire in 5 minutes."
        );

        return true;
    }
    @Transactional
    public boolean resetPasswordWithOtp(String email, String newPassword, String otp) {
        boolean valid = otpService.verifyOtp(email, otp);
        if (!valid) {
            return false;
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public Optional<UserResponseDTO> updateUser(Integer id, UserUpdateRequestDTO dto) throws IOException {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }

        // Session check
        if (id != (Integer) session.getAttribute("userId")) {
            throw new IllegalArgumentException("id does not match the logged-in user");
        }

        User user = userOpt.get();

        System.out.println("QUOC:3");
        // Check if email is being changed and if new email already exists
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()) != null) {
                return Optional.empty();
            }
        }
        System.out.println("QUOC:4");
        // Update fields if provided
        if (dto.getFullName() != null)
            user.setFullName(dto.getFullName());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
        if (dto.getPasswordHash() != null)
            user.setPasswordHash(dto.getPasswordHash());
        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());
        if (dto.getGender() != null)
            user.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null)
            user.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getAvatarUrl() != null) {
            // Delete old avatar file if exists
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                String[] parts = user.getAvatarUrl().split("/");
                String oldFileName = parts[parts.length - 1];
                try {
                    System.out.println("QUOC:5");
                    fileStorageService.deleteFile(oldFileName);
                    System.out.println("QUOC:6");
                } catch (IOException e) {
                    // Log error but continue
                }
            }
            user.setAvatarUrl(dto.getAvatarUrl());
        }
        if (dto.getCoverPhotoUrl() != null){
            // Delete old cover photo file if exists
            if (user.getCoverPhotoUrl() != null && !user.getCoverPhotoUrl().isEmpty()) {
                String[] parts = user.getCoverPhotoUrl().split("/");
                String oldFileName = parts[parts.length - 1];
                try {
                    fileStorageService.deleteFile(oldFileName);
                } catch (IOException e) {
                    // Log error but continue
                    System.err.println("Failed to delete old cover photo file: " + e.getMessage());
                }
            }
            user.setCoverPhotoUrl(dto.getCoverPhotoUrl());
        }
        if (dto.getBio() != null)
            user.setBio(dto.getBio());
        if (dto.getSchoolId() != null)
            user.setSchoolId(dto.getSchoolId());
        if (dto.getAcademicYear() != null)
            user.setAcademicYear(dto.getAcademicYear());
        if (dto.getRole() != null)
            user.setRole(dto.getRole());
        if (dto.getPhone() != null)
            user.setPhone(dto.getPhone());
        if (dto.getWebsite() != null)
            user.setWebsite(dto.getWebsite());
        if (dto.getCountry() != null)
            user.setCountry(dto.getCountry());
        if (dto.getCity() != null)
            user.setCity(dto.getCity());
        if (dto.getEducation() != null)
            user.setEducation(dto.getEducation());
        if (dto.getWorkplace() != null)
            user.setWorkplace(dto.getWorkplace());
        if (dto.getFacebookUrl() != null)
            user.setFacebookUrl(dto.getFacebookUrl());
        if (dto.getInstagramUrl() != null)
            user.setInstagramUrl(dto.getInstagramUrl());
        if (dto.getLinkedinUrl() != null)
            user.setLinkedinUrl(dto.getLinkedinUrl());
        if (dto.getTwitterUrl() != null)
            user.setTwitterUrl(dto.getTwitterUrl());
        // Update department if provided
        if (dto.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());
            department.ifPresent(user::setDepartment);
        }
        user.setUpdatedAt(Instant.now());
        User savedUser = userRepository.save(user);

        return Optional.of(convertToResponseDTO(savedUser));
    }

    @Transactional
    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCoverPhotoUrl(user.getCoverPhotoUrl());
        dto.setBio(user.getBio());
        dto.setSchoolId(user.getSchoolId());
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getId() : null);
        dto.setAcademicYear(user.getAcademicYear());
        dto.setRole(user.getRole());
        dto.setPhone(user.getPhone());
        dto.setWebsite(user.getWebsite());
        dto.setCountry(user.getCountry());
        dto.setCity(user.getCity());
        dto.setEducation(user.getEducation());
        dto.setWorkplace(user.getWorkplace());
        dto.setFacebookUrl(user.getFacebookUrl());
        dto.setInstagramUrl(user.getInstagramUrl());
        dto.setLinkedinUrl(user.getLinkedinUrl());
        dto.setTwitterUrl(user.getTwitterUrl());
        dto.setLastSeenAt(user.getLastSeenAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public Optional<User> login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    /**
     * Update last_seen_at của user để maintain online status
     * 
     * @param userId ID của user cần update
     * @return true nếu update thành công, false nếu không
     */
    public boolean updateLastSeen(Integer userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setLastSeenAt(Instant.now());
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            // Log error if needed
            return false;
        }
    }

    /**
     * Kiểm tra user có đang online không dựa trên last_seen_at
     * User được coi là online nếu last_seen_at trong vòng 3 phút
     * 
     * @param userId ID của user cần kiểm tra
     * @return true nếu online, false nếu offline hoặc user không tồn tại
     */
    public boolean isUserOnline(Integer userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Instant lastSeen = user.getLastSeenAt();

                if (lastSeen == null) {
                    return false; // Chưa bao giờ hoạt động
                }

                // Online threshold: 3 phút
                Instant threshold = Instant.now().minusSeconds(180); // 3 * 60 = 180 seconds
                return lastSeen.isAfter(threshold);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Optional<UserResponseDTO> upgradeUserRole(Integer id, String toRole) {
        // Check user thực hiện hành động có phải admin không
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        String sessionUserRole = (String) session.getAttribute("role");
        if (sessionUserId == null || sessionUserRole == null || !sessionUserRole.equals("admin")) {
            throw new IllegalArgumentException("Only admin can upgrade user roles");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        user.setRole(toRole);
        user.setUpdatedAt(Instant.now());
        User savedUser = userRepository.save(user);
        return Optional.of(convertToResponseDTO(savedUser));
    }

    // ============================================
    // OAuth2 Methods
    // ============================================

    /**
     * Find user by OAuth provider and OAuth ID
     */
    public User findByOAuthProviderAndOAuthId(String provider, String oauthId) {
        return userRepository.findByOauthProviderAndOauthId(provider, oauthId);
    }

    /**
     * Create new user from OAuth2 login (Google)
     * Option 1: Merge accounts if email exists
     */
    @Transactional
    public User createOrUpdateOAuthUser(String email, String name, String picture, String oauthProvider, String oauthId) {
        // PRIORITY 1: Check if OAuth user already exists (by provider + oauthId)
        User oauthUser = userRepository.findByOauthProviderAndOauthId(oauthProvider, oauthId);
        if (oauthUser != null) {
            // OAuth user exists - update info
            oauthUser.setFullName(name);
            oauthUser.setEmail(email); // Update email in case it changed
            // Don't update avatar - keep default avatar
            oauthUser.setUpdatedAt(Instant.now());
            
            System.out.println("✅ OAuth user exists - updating: " + email + " (ID: " + oauthUser.getId() + ")");
            return userRepository.save(oauthUser);
        }
        
        // PRIORITY 2: Check if user with same email exists (for account merging)
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            // Only merge if user doesn't have OAuth linked yet
            if (existingUser.getOauthProvider() == null || existingUser.getOauthId() == null) {
                existingUser.setOauthProvider(oauthProvider);
                existingUser.setOauthId(oauthId);
                existingUser.setUpdatedAt(Instant.now());
                
                System.out.println("✅ Merging OAuth into existing account: " + email + " (ID: " + existingUser.getId() + ")");
                return userRepository.save(existingUser);
            } else {
                // User already has different OAuth linked - this shouldn't happen
                System.err.println("⚠️ User already has OAuth linked: " + email);
                throw new RuntimeException("Email already linked to another OAuth account");
            }
        }
        
        // PRIORITY 3: Create new user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(name);
        newUser.setAvatarUrl("http://localhost:8080/default-avatar.png");  // Always use default avatar
        newUser.setOauthProvider(oauthProvider);
        newUser.setOauthId(oauthId);
        newUser.setPasswordHash(null);  // No password for OAuth users
        newUser.setRole("student");  // Default role
        newUser.setCreatedAt(Instant.now());
        newUser.setUpdatedAt(Instant.now());
        
        System.out.println("✅ Creating new OAuth user: " + email);
        return userRepository.save(newUser);
    }

}