package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import qdt.hcmute.vn.dqtbook_backend.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/presence")
public class PresenceController {

    private final UserService userService;

    @Autowired
    private HttpSession session;

    public PresenceController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Heartbeat endpoint để cập nhật last_seen_at của user
     * Frontend sẽ call endpoint này định kỳ để maintain online status
     */
    @PostMapping("/heartbeat")
    public ResponseEntity<?> updatePresence() {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "User not logged in"));
            }

            // Update last_seen_at to current time
            boolean updated = userService.updateLastSeen(userId);

            if (updated) {
                return ResponseEntity.ok(Map.of(
                        "message", "Presence updated successfully",
                        "userId", userId,
                        "timestamp", System.currentTimeMillis()));
            } else {
                return ResponseEntity.status(500)
                        .body(Map.of("message", "Failed to update presence"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Get online status of specific user
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getUserOnlineStatus(@PathVariable Integer userId) {
        try {
            Integer sessionUserId = (Integer) session.getAttribute("userId");
            if (sessionUserId == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "User not logged in"));
            }

            boolean isOnline = userService.isUserOnline(userId);

            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "isOnline", isOnline,
                    "status", isOnline ? "online" : "offline"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Internal server error: " + e.getMessage()));
        }
    }
}