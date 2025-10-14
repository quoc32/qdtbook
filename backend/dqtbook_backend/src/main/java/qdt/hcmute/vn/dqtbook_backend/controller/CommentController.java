package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse;
import qdt.hcmute.vn.dqtbook_backend.dto.PostCommentResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.model.Comment;
import qdt.hcmute.vn.dqtbook_backend.service.CommentService;

import java.util.Optional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    private HttpSession session;

    public Boolean checkPermission(Integer actionUserId) {
        if (actionUserId != (Integer)session.getAttribute("userId")) {
            return false;
        }
        return true;
    }

    @GetMapping
    public List<PostCommentResponseDTO> list(@PathVariable Integer postId) {
        return commentService.getCommentsForPost(postId);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Integer postId, @RequestBody Comment comment) {
        if (!checkPermission(comment.getAuthor().getId())) {
            throw new IllegalArgumentException("You do not have permission to perform this action");
        }
        Optional<Comment> createdOpt = commentService.createComment(postId, comment);
        if (createdOpt.isPresent()) {
            return ResponseEntity.status(201).body(
                Map.<String, Object>of(
                    "message", "Comment created successfully",
                    "commentId", createdOpt.get().getId()
                )
            );
        }
        return ResponseEntity.status(404).body(new ErrorResponse(404, "Post or user not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer postId, @PathVariable Integer id, @RequestBody Comment comment) {
        try {
            var updatedOpt = commentService.updateComment(postId, id, comment);
            if (updatedOpt.isPresent()) return ResponseEntity.ok(updatedOpt.get());
            return ResponseEntity.status(404).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(404, "Comment not found for this post"));
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(rse.getStatusCode().value(), rse.getReason()));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(400).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(400, iae.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer postId, @PathVariable Integer id) {
        try {
            commentService.deleteComment(postId, id);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(rse.getStatusCode().value(), rse.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }
}
