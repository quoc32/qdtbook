package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qdt.hcmute.vn.dqtbook_backend.dto.PostCommentResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.service.CommentService;

import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentLookupController {

    private final CommentService commentService;

    public CommentLookupController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Integer id) {
        Optional<PostCommentResponseDTO> opt = commentService.getCommentById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("message", "Comment not found"));
        return ResponseEntity.ok(opt.get());
    }
}
