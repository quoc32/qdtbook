package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;

import qdt.hcmute.vn.dqtbook_backend.dto.PostCommentResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.model.Comment;
import qdt.hcmute.vn.dqtbook_backend.model.Post;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.CommentRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.PostRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
        commentRepository.deleteById(id);
    }
}
