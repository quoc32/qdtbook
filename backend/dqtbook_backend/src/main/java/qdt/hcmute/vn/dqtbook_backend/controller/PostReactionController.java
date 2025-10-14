package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import qdt.hcmute.vn.dqtbook_backend.model.PostReaction;
import qdt.hcmute.vn.dqtbook_backend.service.PostReactionService;
import qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse;
import qdt.hcmute.vn.dqtbook_backend.dto.PostReactionResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.exception.DuplicateReactionException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/reactions")
public class PostReactionController {
    private final PostReactionService postReactionService;

    @Autowired
    private HttpSession session;

    public Boolean checkPermission(Integer actionUserId) {
        if (actionUserId != (Integer)session.getAttribute("userId")) {
            throw new IllegalArgumentException("You do not have permission to perform this action");
        }
        return true;
    }

    public PostReactionController(PostReactionService postReactionService) {
        this.postReactionService = postReactionService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> list(@PathVariable Integer postId) {
        try {
            List<PostReactionResponseDTO> reactions = postReactionService.getReactionsForPost(postId);
            return ResponseEntity.ok(reactions);
        } catch (qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException rnfe) {
            return ResponseEntity.status(404).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(404, rnfe.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Integer postId, @RequestBody PostReaction reaction) {
        // validate author_id matches session userId
        try {
            checkPermission(reaction.getUser().getId());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(403).body(new ErrorResponse(403, iae.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal server error"));
        }

        try {
            var createdOpt = postReactionService.createReaction(postId, reaction);
            if (createdOpt.isPresent()) {
                return ResponseEntity.status(201).body(
                    Map.<String, Object>of(
                        "message", "Reaction created or updated successfully",
                        "reactionId", createdOpt.get().getId()
                    )
                );
            }
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Post or user not found"));
        } catch (DuplicateReactionException dre) {
            return ResponseEntity.status(409).body(new ErrorResponse(409, dre.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal server error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            postReactionService.deleteReaction(id);
            return ResponseEntity.noContent().build();
        } catch (qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException rnfe) {
            return ResponseEntity.status(404).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(404, rnfe.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }
}
