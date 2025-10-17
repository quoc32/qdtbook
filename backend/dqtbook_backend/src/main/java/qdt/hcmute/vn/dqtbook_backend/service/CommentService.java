package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;

import qdt.hcmute.vn.dqtbook_backend.dto.PostCommentResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.model.Comment;
import qdt.hcmute.vn.dqtbook_backend.model.Post;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.CommentRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.PostRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    @Autowired
    private HttpSession session;

    public CommentService(CommentRepository commentRepository, 
                        PostRepository postRepository, 
                        UserRepository userRepository,
                        NotificationRepository notificationRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<PostCommentResponseDTO> getCommentsForPost(Integer postId) {
        // ensure post exists
        if (!postRepository.existsById(postId)) {
            throw new qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException("Post not found");
        }
        List<Comment> comments =  commentRepository.findByPostId(postId);
        if (comments == null || comments.isEmpty()) {
            // Trả về list rỗng thay vì ném ngoại lệ
            return List.of();
        }
        List<PostCommentResponseDTO> dtoComments = comments.stream().map((comment) -> {
            PostCommentResponseDTO dtoComment = new PostCommentResponseDTO();
            dtoComment.setId(comment.getId());
            dtoComment.setPostId(comment.getPost().getId());
            dtoComment.setContent(comment.getContent());
            if (comment.getParentComment() != null) {
                dtoComment.setParentCommentId(comment.getParentComment().getId());
            }
            dtoComment.setAuthorId(comment.getAuthor().getId());
            dtoComment.setCreatedAt(comment.getCreatedAt());
            dtoComment.setUpdatedAt(comment.getUpdatedAt());

            return dtoComment;
        }).toList();
        return dtoComments;
    }

    @Transactional
    public Optional<Comment> createComment(Integer postId, Comment comment) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) return Optional.empty();
        Post post = postOpt.get();

        // validate author exists
        if (comment == null || comment.getAuthor() == null || comment.getAuthor().getId() == null) {
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findById(comment.getAuthor().getId());
        if (userOpt.isEmpty()) return Optional.empty();

        // handle parent comment if provided
        if (comment.getParentCommentId() != null) {
            Optional<Comment> parentOpt = commentRepository.findById(comment.getParentCommentId());
            if (parentOpt.isEmpty()) return Optional.empty();
            comment.setParentComment(parentOpt.get());
        }

        // Thêm Notification cho tác giả bài viết khi có bình luận mới
        if (!post.getAuthor().getId().equals(userOpt.get().getId())) {
            String notificationContent = userOpt.get().getFullName() + " đã bình luận về bài viết của bạn.";
            String type = "new_comment";
            // Gọi phương thức tạo thông báo
            // Giả sử sourceId là ID của bài viết
            notificationRepository.createNotification(post.getAuthor().getId(), notificationContent, type, post.getId());
        }

        comment.setPost(post);
        comment.setAuthor(userOpt.get());
        if (comment.getCreatedAt() == null) comment.setCreatedAt(Instant.now());

        Comment saved = commentRepository.saveAndFlush(comment);
        return Optional.of(saved);
    }

    public Optional<Comment> updateComment(Integer postId, Integer id, Comment updated) {
        // ensure post exists first
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();
        Comment existing = opt.get();
        // ensure the comment belongs to the post in the path
        if (existing.getPost() == null || existing.getPost().getId() == null || !existing.getPost().getId().equals(postId)) {
            // treat as not found when mismatch
            return Optional.empty();
        }
        // Permission check: only the author of the comment can update it
        try {
            Object sid = session.getAttribute("userId");
            if (sid == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
            }
            Integer sessionUserId = (Integer) sid;
            if (existing.getAuthor() == null || existing.getAuthor().getId() == null || !existing.getAuthor().getId().equals(sessionUserId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to modify this comment");
            }
        } catch (ClassCastException cce) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid session user");
        }
        existing.setContent(updated.getContent());
        existing.setUpdatedAt(Instant.now());
        return Optional.of(commentRepository.save(existing));
    }

    public void deleteComment(Integer postId, Integer id) {
        // ensure post exists
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        Comment existing = opt.get();
        if (existing.getPost() == null || existing.getPost().getId() == null || !existing.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found for this post");
        }
        // Permission check: only the author of the comment can delete it
        try {
            Object sid = session.getAttribute("userId");
            if (sid == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
            }
            Integer sessionUserId = (Integer) sid;
            if (existing.getAuthor() == null || existing.getAuthor().getId() == null || !existing.getAuthor().getId().equals(sessionUserId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
            }
        } catch (ClassCastException cce) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid session user");
        }

        commentRepository.deleteById(id);
    }

    /**
     * Return a single comment as PostCommentResponseDTO, if exists.
     */
    public Optional<PostCommentResponseDTO> getCommentById(Integer id) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();
        Comment comment = opt.get();
        PostCommentResponseDTO dto = new PostCommentResponseDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost() != null ? comment.getPost().getId() : null);
        dto.setContent(comment.getContent());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        dto.setAuthorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null);
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return Optional.of(dto);
    }
}
