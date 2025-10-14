package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import qdt.hcmute.vn.dqtbook_backend.model.PostReaction;
import qdt.hcmute.vn.dqtbook_backend.model.Post;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.PostReactionRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.PostRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;
import qdt.hcmute.vn.dqtbook_backend.dto.PostReactionResponseDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostReactionService {
    private final PostReactionRepository postReactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostReactionService(PostReactionRepository postReactionRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostReactionResponseDTO> getReactionsForPost(Integer postId) {
        // ensure post exists
        if (!postRepository.existsById(postId)) {
            throw new qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException("Post not found");
        }
        List<PostReaction> reactions = postReactionRepository.findByPostId(postId);
        if (reactions == null || reactions.isEmpty()) {
            // Trả về list rỗng thay vì ném ngoại lệ
            return List.of();
            // throw new qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException("No reactions for this post");
        }
        List<PostReactionResponseDTO> dtoReactions = reactions.stream().map((reaction) -> {
            PostReactionResponseDTO dtoReaction = new PostReactionResponseDTO();
            dtoReaction.setId(reaction.getId());
            dtoReaction.setReactionType(reaction.getReactionType());
            dtoReaction.setCreatedAt(reaction.getCreatedAt());
            if (reaction.getUser() != null) {
                dtoReaction.setAuthorId(reaction.getUser().getId());
            }
            if (reaction.getPost() != null) {
                dtoReaction.setPostId(reaction.getPost().getId());
            }

            return dtoReaction;
        }).toList();
        return dtoReactions;
    }

    @Transactional
    public Optional<PostReaction> createReaction(Integer postId, PostReaction reaction) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) return Optional.empty();
        Post post = postOpt.get();
        // validate user exists
        if (reaction.getUser() == null || reaction.getUser().getId() == null) return Optional.empty();
        Optional<User> userOpt = userRepository.findById(reaction.getUser().getId());
        if (userOpt.isEmpty()) return Optional.empty();
        // validate reaction type (not null) and normalize
        if (reaction.getReactionType() == null || reaction.getReactionType().trim().isEmpty()) {
            reaction.setReactionType("like");
        } else {
            String t = reaction.getReactionType().trim().toLowerCase();
            switch (t) {
                case "like":
                case "love":
                case "haha":
                case "sad":
                case "angry":
                    reaction.setReactionType(t);
                    break;
                default:
                    // unknown type -> default to like
                    reaction.setReactionType("like");
            }
        }
        reaction.setPost(post);
        reaction.setUser(userOpt.get());
        // check duplicate: same user already reacted to this post
        Optional<PostReaction> existing = postReactionRepository.findByPostIdAndUserId(postId, userOpt.get().getId());
        if (existing.isPresent()) {
            // ! update existing reaction
            PostReaction toUpdate = existing.get();
            toUpdate.setReactionType(reaction.getReactionType());
            toUpdate.setPost(post);
            toUpdate.setUser(userOpt.get());
            PostReaction saved = postReactionRepository.saveAndFlush(toUpdate);
            return Optional.of(saved);
        }
        if (reaction.getCreatedAt() == null) reaction.setCreatedAt(Instant.now());
        PostReaction saved = postReactionRepository.saveAndFlush(reaction);
        return Optional.of(saved);
    }

    public void deleteReaction(Integer id) {
        if (!postReactionRepository.existsById(id)) {
            throw new qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException("Reaction not found");
        }
        postReactionRepository.deleteById(id);
    }
}

