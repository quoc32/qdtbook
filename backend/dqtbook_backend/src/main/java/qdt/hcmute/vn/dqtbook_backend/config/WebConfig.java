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
                        "/auth/login", 
                        "/auth/logout",  // cho phép logout mà không cần session
                        "/users/register",  // đăng ký tài khoản
                        "/error", 
                        "/css/**", 
                        "/js/**", 
                        "/images/**"
                ); // bỏ qua các path không cần check session
    }
}
