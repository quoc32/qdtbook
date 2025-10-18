package qdt.hcmute.vn.dqtbook_backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;

import java.io.IOException;

/**
 * OAuth2 Login Success Handler
 * Xử lý sau khi OAuth2 login thành công
 * Tạo session giống như login thường 
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // Get OAuth2User from authentication
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extract email from OAuth2User
        String email = oauth2User.getAttribute("email");
        
        if (email == null) {
            System.err.println("❌ OAuth2 login failed: Email not found");
            response.sendRedirect("/login?error=oauth2_email_missing");
            return;
        }

        // Find user in database by email
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            System.err.println("❌ OAuth2 login failed: User not found for email: " + email);
            response.sendRedirect("/login?error=oauth2_user_not_found");
            return;
        }

        // Create session (same as normal login)
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole());

        // Redirect to home page (root path)
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
