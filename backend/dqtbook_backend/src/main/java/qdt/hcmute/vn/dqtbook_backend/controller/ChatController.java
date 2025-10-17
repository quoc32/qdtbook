package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.dto.*;
import qdt.hcmute.vn.dqtbook_backend.service.ChatService;

import java.util.List;
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

    /**
     * Find or create a group chat with specific members
     * This ensures uniqueness - only one group chat exists for a specific set of users
     * If a group with the exact same members exists, return that chat
     * Otherwise, create a new group chat
     * 
     * @param request The group chat request DTO containing member IDs and optional chat name
     * @return Group chat response with chat information
     */
    @PostMapping("/find-or-create-group")
    public ResponseEntity<?> findOrCreateGroupChat(@RequestBody GroupChatRequestDTO request) {
        // Validate request
        if (request == null || request.getMemberIds() == null || request.getMemberIds().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Member IDs are required"));
        }

        // Validate minimum members (at least 3 for a group)
        if (request.getMemberIds().size() < 3) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Group chat requires at least 3 members"));
        }

        // Validate maximum members (optional, you can adjust this limit)
        if (request.getMemberIds().size() > 100) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Group chat cannot have more than 100 members"));
        }

        var chatOptional = chatService.findOrCreateGroupChat(request);
        if (chatOptional.isPresent()) {
            GroupChatResponseDTO response = chatOptional.get();
            
            // Return 200 OK if chat already exists, 201 CREATED if newly created
            if (response.getIsNewlyCreated()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to create or find group chat. Please check if all users exist."));
        }
    }

    /**
     * Get all group chats that a user is a member of
     * 
     * @param userId User ID (path variable)
     * @return List of group chats the user belongs to
     */
    @GetMapping("/groups/user/{userId}")
    public ResponseEntity<?> getGroupChatsByUserId(@PathVariable Integer userId) {
        // Validate userId
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid user ID"));
        }

        List<GroupChatListResponseDTO> groupChats = chatService.getGroupChatsByUserId(userId);
        return ResponseEntity.ok(groupChats);
    }
}