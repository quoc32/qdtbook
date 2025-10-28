package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import qdt.hcmute.vn.dqtbook_backend.model.PostShare;
import qdt.hcmute.vn.dqtbook_backend.model.Post;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.model.Notification;
import qdt.hcmute.vn.dqtbook_backend.repository.PostShareRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.PostRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.NotificationRepository;

import java.util.Optional;
import java.util.List;
import qdt.hcmute.vn.dqtbook_backend.dto.PostShareRequest;

@Service
public class PostShareService {
    private final PostShareRepository postShareRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public PostShareService(PostShareRepository postShareRepository, PostRepository postRepository, UserRepository userRepository, NotificationRepository notificationRepository) {
        this.postShareRepository = postShareRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<PostShare> listSharesForPost(Integer postId) {
        return postShareRepository.findByPostId(postId);
    }

    @Transactional
    public Optional<PostShare> createShare(Integer postId, PostShareRequest payload) {
    Optional<Post> postOpt = postRepository.findById(postId);
    if (postOpt.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Post not found");
    Post post = postOpt.get();

    if (payload.getUserId() == null) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "userId is required");
    Optional<User> userOpt = userRepository.findById(payload.getUserId());
    if (userOpt.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found");

        // try to find existing share for this post+user
        Optional<PostShare> existingOpt = postShareRepository.findByPostIdAndUserId(postId, payload.getUserId());
        PostShare share;
        if (existingOpt.isPresent()) {
            // update existing share
            share = existingOpt.get();
            share.setSharedText(payload.getSharedText());
            share.setVisibility(payload.getVisibility());
            share.setUpdatedAt(java.time.Instant.now());
        } else {
            share = new PostShare();
            share.setPost(post);
            share.setUser(userOpt.get());
            share.setSharedText(payload.getSharedText());
            share.setVisibility(payload.getVisibility());
        }

        PostShare saved;
        try{
            saved = postShareRepository.save(share);
        }catch(DataIntegrityViolationException dive){
            // possible duplicate inserted concurrently; try to return existing
            Optional<PostShare> existingAfter = postShareRepository.findByPostIdAndUserId(postId, payload.getUserId());
            if(existingAfter.isPresent()) saved = existingAfter.get();
            else throw dive;
        }

        // create notification for owner of the post (if different user)
        try {
            User postOwner = post.getAuthor();
            if (postOwner != null && !postOwner.getId().equals(userOpt.get().getId())) {
                Notification n = new Notification();
                // n.setRecipient(postOwner); // !: recipient is user id now
                n.setRecipientId(postOwner.getId());
                // n.setSender(userOpt.get()); // !: sender is user id now
                n.setSenderId(userOpt.get().getId());
                n.setType("post_share");
                n.setSourceId(saved.getId());
                n.setIsRead(false);
                notificationRepository.save(n);
            }
        } catch (Exception ignored) {}

        return Optional.of(saved);
    }

    // Create share using the currently logged-in user (server-side session). The payload's userId is ignored.
    @Transactional
    public Optional<PostShare> createShareAsUser(Integer postId, Integer currentUserId, PostShareRequest payload) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Post not found");
        Post post = postOpt.get();

        if (currentUserId == null) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        Optional<User> userOpt = userRepository.findById(currentUserId);
        if (userOpt.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found");

        // upsert: update existing share if present
        Optional<PostShare> existingOpt = postShareRepository.findByPostIdAndUserId(postId, currentUserId);
        PostShare share;
        if (existingOpt.isPresent()) {
            share = existingOpt.get();
            share.setSharedText(payload.getSharedText());
            share.setVisibility(payload.getVisibility());
            share.setUpdatedAt(java.time.Instant.now());
        } else {
            share = new PostShare();
            share.setPost(post);
            share.setUser(userOpt.get());
            share.setSharedText(payload.getSharedText());
            share.setVisibility(payload.getVisibility());
        }

        PostShare saved;
        try{
            saved = postShareRepository.save(share);
        }catch(DataIntegrityViolationException dive){
            // concurrent insert raced us; return the existing record if present
            Optional<PostShare> existingAfter = postShareRepository.findByPostIdAndUserId(postId, currentUserId);
            if(existingAfter.isPresent()) saved = existingAfter.get();
            else throw dive;
        }

        // create notification for owner of the post (if different user)
        try {
            User postOwner = post.getAuthor();
            if (postOwner != null && !postOwner.getId().equals(userOpt.get().getId())) {
                Notification n = new Notification();
                n.setRecipientId(postOwner.getId());
                n.setSenderId(userOpt.get().getId());
                n.setType("post_share");
                n.setSourceId(saved.getId());
                n.setIsRead(false);
                notificationRepository.save(n);
            }
        } catch (Exception ignored) {}

        return Optional.of(saved);
    }

    // Delete share but check that the current user is the owner OR has admin role.
    public void deleteShareAsUser(Integer postId, Integer id, Integer currentUserId, String role) {
        // ensure post exists
        if (!postRepository.existsById(postId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Post not found");
        }
        Optional<PostShare> opt = postShareRepository.findById(id);
        if (opt.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found");
        }
        PostShare existing = opt.get();
        if (existing.getPost() == null || existing.getPost().getId() == null || !existing.getPost().getId().equals(postId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found for this post");
        }

        // ownership check: allow if currentUserId equals share owner OR role equals "ADMIN" (case-insensitive)
        Integer ownerId = existing.getUser() != null ? existing.getUser().getId() : null;
        boolean isAdmin = role != null && "ADMIN".equalsIgnoreCase(role);
        if (ownerId == null || (!ownerId.equals(currentUserId) && !isAdmin)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Không có quyền xóa share này");
        }

        postShareRepository.deleteById(id);
    }

    public void deleteShare(Integer postId, Integer id) {
        // ensure post exists
        if (!postRepository.existsById(postId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Post not found");
        }
        Optional<PostShare> opt = postShareRepository.findById(id);
        if (opt.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found");
        }
        PostShare existing = opt.get();
        if (existing.getPost() == null || existing.getPost().getId() == null || !existing.getPost().getId().equals(postId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found for this post");
        }
        postShareRepository.deleteById(id);
    }

    public PostShare getShare(Integer postId, Integer id) {
        Optional<PostShare> opt = postShareRepository.findById(id);
        if (opt.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found");
        PostShare existing = opt.get();
        if (existing.getPost() == null || existing.getPost().getId() == null || !existing.getPost().getId().equals(postId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found for this post");
        }
        return existing;
    }

    // Get share by id only (used by admin/fallback or when postId is unknown in the client)
    public PostShare getShareById(Integer id) {
        Optional<PostShare> opt = postShareRepository.findById(id);
        if (opt.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found");
        }
        return opt.get();
    }

    // Delete share by id only (owner or admin). Does not require postId.
    public void deleteShareByIdAsUser(Integer id, Integer currentUserId, String role) {
        if (currentUserId == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }
        Optional<PostShare> opt = postShareRepository.findById(id);
        if (opt.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found");
        }
        PostShare existing = opt.get();
        Integer ownerId = existing.getUser() != null ? existing.getUser().getId() : null;
        boolean isAdmin = role != null && "ADMIN".equalsIgnoreCase(role);
        if (ownerId == null || (!ownerId.equals(currentUserId) && !isAdmin)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Không có quyền xóa share này");
        }
        postShareRepository.deleteById(id);
    }

    @Transactional
    public PostShare updateShareAsUser(Integer postId, Integer shareId, Integer currentUserId, PostShareRequest payload) {
        if (currentUserId == null) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        Optional<PostShare> opt = postShareRepository.findById(shareId);
        if (opt.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found");
        PostShare existing = opt.get();
        if (existing.getPost() == null || existing.getPost().getId() == null || !existing.getPost().getId().equals(postId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Share not found for this post");
        }
        Integer ownerId = existing.getUser() != null ? existing.getUser().getId() : null;
        if (ownerId == null || !ownerId.equals(currentUserId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Không có quyền sửa share này");
        }
        // Update allowed fields
        if (payload.getSharedText() != null) existing.setSharedText(payload.getSharedText());
        if (payload.getVisibility() != null) existing.setVisibility(payload.getVisibility());
        existing.setUpdatedAt(java.time.Instant.now());
        return postShareRepository.save(existing);
    }
}
