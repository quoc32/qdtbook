package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import qdt.hcmute.vn.dqtbook_backend.model.User;

/**
 * Custom OAuth2 User Service
 * Xử lý user info từ Google sau khi login thành công
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Get OAuth2User from Google
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extract user info from Google
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String sub = oauth2User.getAttribute("sub");  // Google User ID
        Boolean emailVerified = oauth2User.getAttribute("email_verified");

        // Validate email is verified
        if (emailVerified == null || !emailVerified) {
            throw new OAuth2AuthenticationException("Email not verified by Google");
        }

        // Create or update user in database
        User user = userService.createOrUpdateOAuthUser(
            email,
            name,
            picture,
            "google",
            sub
        );

        System.out.println("✅ OAuth2 User loaded: " + email + " (ID: " + user.getId() + ")");

        // Return OAuth2User for Spring Security to process
        return oauth2User;
    }
}
