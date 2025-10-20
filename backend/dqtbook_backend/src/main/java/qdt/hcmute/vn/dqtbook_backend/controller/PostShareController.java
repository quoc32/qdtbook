package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.model.PostShare;
import qdt.hcmute.vn.dqtbook_backend.service.PostShareService;
import qdt.hcmute.vn.dqtbook_backend.dto.PostShareRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/shares")
public class PostShareController {
    private final PostShareService postShareService;

    public PostShareController(PostShareService postShareService) {
        this.postShareService = postShareService;
    }

    @GetMapping
    public List<PostShare> list(@PathVariable Integer postId) {
        return postShareService.listSharesForPost(postId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer postId, @PathVariable Integer id) {
        try {
            var ps = postShareService.getShare(postId, id);
            return ResponseEntity.ok(ps);
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(rse.getStatusCode().value(), rse.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }

    // Create share: require logged-in user via HttpSession (session attribute "userId").
    @PostMapping
    public ResponseEntity<?> create(@PathVariable Integer postId, @RequestBody PostShareRequest payload, jakarta.servlet.http.HttpSession session) {
        try {
            Object sessionUserId = session.getAttribute("userId");
            if (sessionUserId == null) {
                return ResponseEntity.status(401).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(401, "Chưa đăng nhập"));
            }
            Integer currentUserId = (Integer) sessionUserId;
            var createdOpt = postShareService.createShareAsUser(postId, currentUserId, payload);
            if (createdOpt.isPresent()) {
                var ps = createdOpt.get();
                return ResponseEntity.created(URI.create("/posts/" + postId + "/shares/" + ps.getId())).body(ps);
            }
            return ResponseEntity.status(400).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(400, "Invalid payload or missing post"));
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(rse.getStatusCode().value(), rse.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }

    // Delete share: require logged-in user and ownership (or role=admin stored in session attribute "role").
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer postId, @PathVariable Integer id, jakarta.servlet.http.HttpSession session) {
        try {
            Object sessionUserId = session.getAttribute("userId");
            if (sessionUserId == null) {
                return ResponseEntity.status(401).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(401, "Chưa đăng nhập"));
            }
            Integer currentUserId = (Integer) sessionUserId;
            Object roleObj = session.getAttribute("role");
            String role = roleObj != null ? String.valueOf(roleObj) : null;

            postShareService.deleteShareAsUser(postId, id, currentUserId, role);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(rse.getStatusCode().value(), rse.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }

    // Update share: require logged-in user and ownership. Allows editing sharedText and visibility.
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer postId, @PathVariable Integer id, @RequestBody PostShareRequest payload, jakarta.servlet.http.HttpSession session) {
        try {
            Object sessionUserId = session.getAttribute("userId");
            if (sessionUserId == null) {
                return ResponseEntity.status(401).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(401, "Chưa đăng nhập"));
            }
            Integer currentUserId = (Integer) sessionUserId;
            var updated = postShareService.updateShareAsUser(postId, id, currentUserId, payload);
            return ResponseEntity.ok(updated);
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(rse.getStatusCode().value(), rse.getReason()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new qdt.hcmute.vn.dqtbook_backend.dto.ErrorResponse(500, "Internal server error"));
        }
    }
}
