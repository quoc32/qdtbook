package qdt.hcmute.vn.dqtbook_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import qdt.hcmute.vn.dqtbook_backend.dto.AuthResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.service.UserService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String email = request.get("email");
        String password = request.get("password");

        return userService.login(email, password)
                .map(user -> {
                    HttpSession session = httpRequest.getSession(true); // tạo session nếu chưa có
                    session.setAttribute("userId", user.getId());

                    AuthResponseDTO authResponse = new AuthResponseDTO(
                            "Đăng nhập thành công",
                            session.getId(),
                            user.getId(),
                            user.getEmail(),
                            user.getFullName(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getGender(),
                            user.getBio()
                    );

                    return ResponseEntity.ok(authResponse);
                })
                .orElse(ResponseEntity.status(401).body(
                        new AuthResponseDTO(
                            "Sai email hoặc mật khẩu",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                ));
    }

    // Lấy thông tin user hiện tại
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Chưa đăng nhập"));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Đang đăng nhập",
                "userId", userId
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // không tạo mới

        if (session == null) {
            return ResponseEntity.status(400).body(Map.of(
                "message", "Session không tồn tại hoặc đã hết hạn"
            ));
        }

        String oldId = session.getId();
        session.invalidate();

        // Xóa cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
            "message", "Đăng xuất thành công",
            "oldSessionId", oldId
        ));
    }

}
