package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.model.PostReaction;
import qdt.hcmute.vn.dqtbook_backend.service.PostReactionService;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/reactions")
public class PostReactionController {
    private final PostReactionService postReactionService;

    public PostReactionController(PostReactionService postReactionService) {
        this.postReactionService = postReactionService;
    }

    @GetMapping
    public List<PostReaction> list(@PathVariable Integer postId) {
        return postReactionService.getReactionsForPost(postId);
    }

    @PostMapping
    public ResponseEntity<PostReaction> create(@PathVariable Integer postId, @RequestBody PostReaction reaction) {
        return postReactionService.createReaction(postId, reaction)
                .map(r -> ResponseEntity.ok(r))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        postReactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}
