package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.UserResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendActionDTO;
import qdt.hcmute.vn.dqtbook_backend.service.FriendService;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    private HttpSession session;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getFriendsByUserId(@PathVariable Integer userId) {
        Optional<List<FriendResponseDTO>> friends = friendService.getFriendsByUserId(userId);
        if (friends.isPresent()) {
            return ResponseEntity.ok(friends.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDTO dto) {
        // Session user check
        Integer senderId = dto.getSenderId();
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(senderId)) {
            throw new IllegalArgumentException("sender_id does not match the logged-in user");
        }

        Optional<FriendResponseDTO> result = friendService.sendFriendRequest(dto);
        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
        } else {
            return ResponseEntity.badRequest().body("Error sending friend request");
        }
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendActionDTO dto) {
        Optional<FriendResponseDTO> result = friendService.acceptFriendRequest(dto);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.badRequest().body("Error accepting friend request");
        }
    }

    @PutMapping("/refuse")
    public ResponseEntity<?> refuseFriendRequest(@RequestBody FriendActionDTO dto) {
        boolean done = friendService.refuseFriendRequest(dto);
        if (done) {
            return ResponseEntity.ok(Map.of(
                    "message", "Friend request refused successfully",
                    "senderId", dto.getSenderId(),
                    "receiverId", dto.getReceiverId()));
        } else {
            return ResponseEntity.badRequest().body("Error refusing friend request");
        }
    }

    @PutMapping("/block")
    public ResponseEntity<?> blockFriend(@RequestBody FriendActionDTO dto) {
        Optional<FriendResponseDTO> result = friendService.blockFriend(dto);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.badRequest().body("Error blocking friend");
        }
    }

    @DeleteMapping("/unfriend")
    public ResponseEntity<?> unfriend(@RequestBody FriendActionDTO dto) {
        boolean deleted = friendService.unfriend(dto);
        if (deleted) {
            return ResponseEntity.ok(Map.of(
                    "message", "Unfriended successfully",
                    "senderId", dto.getSenderId(),
                    "receiverId", dto.getReceiverId()));
        } else {
            return ResponseEntity.badRequest().body("Error unfriending");
        }

    }

    @DeleteMapping("/cancel_request")
    public ResponseEntity<?> cancel_request(@RequestBody FriendActionDTO dto) {
        boolean deleted = friendService.cancel_request(dto);
        if (deleted) {
            return ResponseEntity.ok(Map.of(
                    "message", "Cancel friend request successfully",
                    "senderId", dto.getSenderId(),
                    "receiverId", dto.getReceiverId()));
        } else {
            return ResponseEntity.badRequest().body("Error unfriending");
        }
    }

    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<?> getFriendSuggestions(@PathVariable Integer userId) {
        Optional<List<UserResponseDTO>> suggestions = friendService.getFriendSuggestions(userId);
        if (suggestions.isPresent()) {
            return ResponseEntity.ok(suggestions.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{userId}/online-status")
    public ResponseEntity<?> getFriendsWithOnlineStatus(@PathVariable Integer userId) {
        try {
            // Debug session info
            Integer sessionUserId = (Integer) session.getAttribute("userId");
            System.out.println("DEBUG - Session userId: " + sessionUserId + ", Request userId: " + userId);

            // Temporarily disable session check for testing
            // TODO: Re-enable this for production
            /*
             * if (sessionUserId != null && !sessionUserId.equals(userId)) {
             * return ResponseEntity.status(HttpStatus.FORBIDDEN)
             * .body(Map.of(
             * "message", "Access denied: User ID does not match session",
             * "sessionUserId", sessionUserId,
             * "requestUserId", userId
             * ));
             * }
             */

            Optional<List<FriendResponseDTO>> friends = friendService.getFriendsWithOnlineStatus(userId);
            if (friends.isPresent()) {
                // Always return the list, even if empty
                List<FriendResponseDTO> friendList = friends.get();
                return ResponseEntity.ok(Map.of(
                        "userId", userId,
                        "friendsCount", friendList.size(),
                        "friends", friendList,
                        "sessionUserId", sessionUserId));
            } else {
                // User not found - return error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found", "userId", userId));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("user/{userId}/count")
    public ResponseEntity<?> countFriends(@PathVariable Integer userId) {
        Optional<Long> count = friendService.countFriends(userId);
        if (count.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "friendCount", count.get()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
