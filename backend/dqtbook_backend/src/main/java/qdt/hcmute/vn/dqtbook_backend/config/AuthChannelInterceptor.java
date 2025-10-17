package qdt.hcmute.vn.dqtbook_backend.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * Interceptor để xác thực mỗi message được gửi qua WebSocket
 */
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Khi client CONNECT, kiểm tra userId trong session attributes
            Object userId = accessor.getSessionAttributes().get("userId");
            
            if (userId == null) {
                System.out.println("🚫 WebSocket CONNECT rejected: No userId in session");
                throw new IllegalArgumentException("Unauthorized: No valid session");
            }
            
            System.out.println("✅ WebSocket CONNECT accepted: userId=" + userId);
        }
        
        return message;
    }
}
