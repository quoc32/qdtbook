package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.dto.*;
import qdt.hcmute.vn.dqtbook_backend.service.ChatService;

import java.util.Map;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Check if a direct chat exists between two users
     * 
     * @param request The check chat request DTO containing userId1 and userId2
     * @return Check chat response with existence status and chat ID if found
     */
    @PostMapping("/check-direct-chat")
    public ResponseEntity<CheckChatResponseDTO> checkDirectChatExists(@RequestBody CheckChatRequestDTO request) {
        // Validate request
        if (request == null || request.getUserId1() == null || request.getUserId2() == null) {
            CheckChatResponseDTO errorResponse = new CheckChatResponseDTO();
            errorResponse.setExists(false);
            errorResponse.setMessage("Both userId1 and userId2 are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Validate users are different
        if (request.getUserId1().equals(request.getUserId2())) {
            CheckChatResponseDTO errorResponse = new CheckChatResponseDTO();
            errorResponse.setExists(false);
            errorResponse.setMessage("Cannot check chat with yourself");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        CheckChatResponseDTO response = chatService.checkDirectChatExists(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Find or create a direct chat between two users
     * This ensures uniqueness - only one chat exists between any two users
     * 
     * @param request The direct chat request DTO containing userId1 and userId2
     * @return Simple chat response with basic information
     */
    @PostMapping("/find-or-create-direct")
    public ResponseEntity<?> findOrCreateDirectChat(@RequestBody DirectChatRequestDTO request) {
        // Validate request
        if (request == null || request.getUserId1() == null || request.getUserId2() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Both userId1 and userId2 are required"));
        }

        // Validate users are different
        if (request.getUserId1().equals(request.getUserId2())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Cannot create chat with yourself"));
        }

        var chatOptional = chatService.findOrCreateDirectChat(request);
        if (chatOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(chatOptional.get());
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to create or find chat. Please check if both users exist."));
        }
    }

    /**
     * Get chat ID between two users
     * 
     * @param request The check chat request DTO containing userId1 and userId2
     * @return Chat ID if found, or error message
     */
    @PostMapping("/get-chat-id")
    public ResponseEntity<?> getChatIdBetweenUsers(@RequestBody CheckChatRequestDTO request) {
        // Validate request
        if (request == null || request.getUserId1() == null || request.getUserId2() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Both userId1 and userId2 are required"));
        }

        // Validate users are different
        if (request.getUserId1().equals(request.getUserId2())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Cannot get chat ID with yourself"));
        }

        var chatIdOptional = chatService.getChatIdBetweenUsers(request.getUserId1(), request.getUserId2());
        if (chatIdOptional.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "chatId", chatIdOptional.get(),
                    "message", "Chat ID found successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}