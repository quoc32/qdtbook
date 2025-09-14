package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.model.PostShare;
import qdt.hcmute.vn.dqtbook_backend.service.PostShareService;
import qdt.hcmute.vn.dqtbook_backend.dto.PostShareRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/shares")
public class PostShareController {
    private final PostShareService postShareService;

    public PostShareController(PostShareService postShareService) {
        this.postShareService = postShareService;
    }

    @GetMapping
    public List<PostShare> list(@PathVariable Integer postId) {
        return postShareService.listSharesForPost(postId);
    }

    @PostMapping
    public ResponseEntity<PostShare> create(@PathVariable Integer postId, @RequestBody PostShareRequest payload) {
        return postShareService.createShare(postId, payload)
                .map(ps -> ResponseEntity.created(URI.create("/posts/" + postId + "/shares/" + ps.getId())).body(ps))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        postShareService.deleteShare(id);
        return ResponseEntity.noContent().build();
    }
}
