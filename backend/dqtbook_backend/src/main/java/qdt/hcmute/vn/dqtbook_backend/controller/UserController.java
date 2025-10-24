package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.dto.UserCreateRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.UserUpdateRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.UserResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.service.UserService;
import java.io.IOException;

import java.util.List;
import java.util.Optional;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final qdt.hcmute.vn.dqtbook_backend.service.PostShareQueryService postShareQueryService;

    public UserController(UserService userService, qdt.hcmute.vn.dqtbook_backend.service.PostShareQueryService postShareQueryService) {
        this.userService = userService;
        this.postShareQueryService = postShareQueryService;
    }

    @GetMapping("/api")
    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Từ khóa tìm kiếm không được để trống"
            ));
        }
        
        List<UserResponseDTO> users = userService.searchUsers(keyword.trim());
        
        if (users.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                "message", "Không tìm thấy người dùng nào phù hợp với từ khóa: " + keyword,
                "data", users
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Tìm thấy " + users.size() + " người dùng",
            "data", users
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Optional<UserResponseDTO> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("find/by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
        Optional<UserResponseDTO> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        userService.sendOtpForRegistration(email);
        return ResponseEntity.ok("OTP sent to email: " + email);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDTO dto) {
        Optional<UserResponseDTO> user = userService.verifyOtpAndCreateUser(dto);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("message", "User already exists"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("new_password");
        String otp = body.get("otp");

        boolean result = userService.resetPasswordWithOtp(email, newPassword, otp);
        if (result) {
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP or email");
        }
    }

    @PostMapping("/send-otp/forgot-password")
    public ResponseEntity<?> sendOtpForgotPassword(@RequestParam String email) {
        userService.sendOtpForForgotPassword(email);
        return ResponseEntity.ok("OTP sent to email: " + email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequestDTO dto) throws IOException {
        System.out.println("QUOC:1");
        try {
            Optional<UserResponseDTO> user = userService.updateUser(id, dto);
            System.out.println("QUOC:2");
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.badRequest().body("Error updating user");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> bodyl) {
        String email = bodyl.get("email");
        boolean deleted = userService.deleteUser(email);
        if (deleted) {
            return ResponseEntity.ok("User with email " + email + " has been deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{id}/shares")
    public ResponseEntity<?> getUserShares(@PathVariable Integer id) {
        try {
            var list = postShareQueryService.listSharesByUser(id);
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }

    @PostMapping("/{id}/upgrade")
    public ResponseEntity<?> upgradeUserRole(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String toRole = body.get("toRole");
        if (toRole == null || (!toRole.equals("special") && !toRole.equals("student"))) {
            return ResponseEntity.badRequest().body("Invalid role specified");
        }

        Optional<UserResponseDTO> user = userService.upgradeUserRole(id, toRole);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or upgrade failed");
        }
    }

    // >> Ban và unBan 1 user (chỉ dành cho admin)
    @PostMapping("/ban")
    public ResponseEntity<?> banUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userService.banUser(email);
        return ResponseEntity.ok("User with email " + email + " has been banned.");
    }
    @PostMapping("/unban")
    public ResponseEntity<?> unbanUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userService.unbanUser(email);
        return ResponseEntity.ok("User with email " + email + " has been unbanned.");
    }
    // >> ==========================================================
}
