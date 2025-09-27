package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendActionDTO;
import qdt.hcmute.vn.dqtbook_backend.service.FriendService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

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
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body("Error unfriending");
        }
    }
}
