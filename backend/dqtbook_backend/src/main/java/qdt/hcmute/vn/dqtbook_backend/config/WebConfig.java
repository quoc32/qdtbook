package qdt.hcmute.vn.dqtbook_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/**")              // áp dụng cho tất cả các request
                .excludePathPatterns(
                        "/api/auth/login", 
                        "/api/auth/logout",     // cho phép logout mà không cần session
                        "/api/users/register",  // đăng ký tài khoản
                        "/api/users/send-otp",  // gửi OTP cho đăng ký tài khoản
                        "/api/users/forgot-password", // quên mật khẩu
                        "/api/users/send-otp/forgot-password", // gửi OTP cho quên mật khẩu
                        "/error", 
                        "/css/**", 
                        "/js/**", 
                        "/images/**",
                        "/views/**",
                        "/home",  // trang chủ
                        "/",
                        "/qdtFavicon.png",
                        "/oauth2/**",           // OAuth2 authorization
                        "/login/oauth2/**",     // OAuth2 callback
                        "/login",                // Spring Security login page
                        "/video/**"
                ); // bỏ qua các path không cần check session
    }
}