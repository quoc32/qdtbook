package qdt.hcmute.vn.dqtbook_backend.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Interceptor để lấy thông tin user từ HTTP session khi handshake WebSocket
 */
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, 
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler, 
                                   Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            
            if (session != null) {
                // Lấy userId từ HTTP session
                Object userId = session.getAttribute("userId");
                
                if (userId != null) {
                    // Lưu userId vào WebSocket session attributes
                    attributes.put("userId", userId);
                    System.out.println("✅ WebSocket handshake: userId=" + userId);
                    return true; // Cho phép kết nối
                }
            }
            
            System.out.println("🚫 WebSocket handshake rejected: No valid session");
            return false; // Từ chối kết nối nếu không có session
        }
        
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, 
                              ServerHttpResponse response,
                              WebSocketHandler wsHandler, 
                              Exception exception) {
        // Có thể log hoặc xử lý sau khi handshake
    }
}
