package qdt.hcmute.vn.dqtbook_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Định nghĩa các tiền tố cho các kênh nhắn tin
        // Kênh '/topic' cho các thông báo chung (ví dụ: tin nhắn trong nhóm chat)
        // Kênh '/queue' cho các tin nhắn riêng tư (ví dụ: tin nhắn 1-1)
        config.enableSimpleBroker("/topic", "/queue");

        // Tiền tố cho các endpoint mà client sẽ gửi tin nhắn đến
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đăng ký endpoint '/ws' mà client sẽ kết nối đến
        // Thêm HttpSessionHandshakeInterceptor để xác thực user từ HTTP session
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // CHỈNH LẠI
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // Xác thực khi handshake
                .withSockJS(); // Hỗ trợ SockJS cho các trình duyệt không hỗ trợ WebSocket natively
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Thêm interceptor để xác thực mỗi message từ client
        registration.interceptors(new AuthChannelInterceptor());
    }
}