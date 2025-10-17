package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import qdt.hcmute.vn.dqtbook_backend.dto.websocket.ChatMessageDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.websocket.ChatMessageResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.websocket.ReadStatusDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.websocket.TypingStatusDTO;
import qdt.hcmute.vn.dqtbook_backend.service.MessageService;

@Controller
public class WebSocketChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    /**
     * Xử lý tin nhắn mới từ client
     * Client sẽ gửi tin nhắn đến /app/chat.sendMessage
     */
    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Lấy userId từ WebSocket session (đã được set bởi HttpSessionHandshakeInterceptor)
            Integer authenticatedUserId = (Integer) headerAccessor.getSessionAttributes().get("userId");
            
            if (authenticatedUserId == null) {
                System.err.println("❌ Unauthorized: No userId in session");
                return; // Từ chối xử lý nếu không có userId
            }
            
            // Validate: senderId phải khớp với authenticated user
            if (!authenticatedUserId.equals(chatMessageDTO.getSenderId())) {
                System.err.println("❌ Forbidden: senderId mismatch. Expected: " + authenticatedUserId + ", Got: " + chatMessageDTO.getSenderId());
                return; // Từ chối nếu senderId không khớp
            }
            
            // Lưu tin nhắn vào database thông qua MessageService
            ChatMessageResponseDTO responseDTO = messageService.saveAndBroadcastMessage(chatMessageDTO);
            
            if (responseDTO == null) {
                System.err.println("❌ Failed to save message for chatId: " + chatMessageDTO.getChatId());
                return;
            }
            
            // Gửi tin nhắn đến tất cả thành viên trong chat
            String destination = "/topic/chat." + chatMessageDTO.getChatId();
            messagingTemplate.convertAndSend(destination, responseDTO);
            
            System.out.println("✅ Message sent successfully: chatId=" + chatMessageDTO.getChatId() + ", senderId=" + authenticatedUserId);
            
        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý cập nhật trạng thái đọc tin nhắn
     * Client sẽ gửi thông báo đến /app/chat.readStatus
     */
    @MessageMapping("/chat.readStatus")
    public void processReadStatus(@Payload ReadStatusDTO readStatusDTO, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Lấy userId từ WebSocket session
            Integer authenticatedUserId = (Integer) headerAccessor.getSessionAttributes().get("userId");
            
            if (authenticatedUserId == null) {
                System.err.println("❌ Unauthorized: No userId in session");
                return;
            }
            
            // Validate: userId phải khớp với authenticated user
            if (!authenticatedUserId.equals(readStatusDTO.getUserId())) {
                System.err.println("❌ Forbidden: userId mismatch for read status");
                return;
            }
            
            // Cập nhật trạng thái đã đọc
            boolean success = messageService.markMessageAsRead(readStatusDTO);
            
            if (!success) {
                System.err.println("❌ Failed to mark message as read: messageId=" + readStatusDTO.getMessageId());
                return;
            }
            
            // Gửi cập nhật trạng thái đến tất cả thành viên trong chat
            String destination = "/topic/chat." + readStatusDTO.getChatId() + ".readStatus";
            messagingTemplate.convertAndSend(destination, readStatusDTO);
            
            System.out.println("✅ Read status updated: messageId=" + readStatusDTO.getMessageId() + ", userId=" + authenticatedUserId);
            
        } catch (Exception e) {
            System.err.println("❌ Error processing read status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý thông báo khi người dùng đang nhập tin nhắn
     * Client sẽ gửi thông báo đến /app/chat.typing
     */
    @MessageMapping("/chat.typing")
    public void processTypingStatus(@Payload TypingStatusDTO typingStatus, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Lấy userId từ WebSocket session
            Integer authenticatedUserId = (Integer) headerAccessor.getSessionAttributes().get("userId");
            
            if (authenticatedUserId == null) {
                System.err.println("❌ Unauthorized: No userId in session");
                return;
            }
            
            // Validate: userId phải khớp với authenticated user
            if (!authenticatedUserId.equals(typingStatus.getUserId())) {
                System.err.println("❌ Forbidden: userId mismatch for typing status");
                return;
            }
            
            // Chuyển tiếp thông báo đang nhập tin nhắn
            String destination = "/topic/chat." + typingStatus.getChatId() + ".typing";
            messagingTemplate.convertAndSend(destination, typingStatus);
            
            System.out.println("✅ Typing status sent: chatId=" + typingStatus.getChatId() + ", userId=" + authenticatedUserId + ", typing=" + typingStatus.isTyping());
            
        } catch (Exception e) {
            System.err.println("❌ Error processing typing status: " + e.getMessage());
            e.printStackTrace();
        }
    }
}