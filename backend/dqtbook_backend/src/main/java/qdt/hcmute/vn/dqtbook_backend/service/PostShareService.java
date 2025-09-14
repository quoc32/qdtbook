package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
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

    public Optional<PostShare> createShare(Integer postId, PostShareRequest payload) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) return Optional.empty();
        Post post = postOpt.get();

        if (payload.getUserId() == null) return Optional.empty();
        Optional<User> userOpt = userRepository.findById(payload.getUserId());
        if (userOpt.isEmpty()) return Optional.empty();

        PostShare share = new PostShare();
        share.setPost(post);
        share.setUser(userOpt.get());
        share.setSharedText(payload.getSharedText());
        share.setVisibility(payload.getVisibility());

        PostShare saved = postShareRepository.save(share);

        // create notification for owner of the post (if different user)
        try {
            User postOwner = post.getAuthor();
            if (postOwner != null && !postOwner.getId().equals(userOpt.get().getId())) {
                Notification n = new Notification();
                n.setRecipient(postOwner);
                n.setSender(userOpt.get());
                n.setType("post_share");
                n.setSourceId(saved.getId());
                n.setIsRead(false);
                notificationRepository.save(n);
            }
        } catch (Exception ignored) {}

        return Optional.of(saved);
    }

    public void deleteShare(Integer id) {
        postShareRepository.deleteById(id);
    }
}
