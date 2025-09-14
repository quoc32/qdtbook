package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import qdt.hcmute.vn.dqtbook_backend.model.Post;
import qdt.hcmute.vn.dqtbook_backend.model.PostMedia;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.PostRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.PostMediaRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;
import qdt.hcmute.vn.dqtbook_backend.dto.PostCreateRequest;
import org.springframework.transaction.annotation.Transactional;
import qdt.hcmute.vn.dqtbook_backend.dto.PostUpdateRequest;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMediaRepository postMediaRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMediaRepository postMediaRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMediaRepository = postMediaRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Integer id) {
        return postRepository.findById(id);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Optional<Post> createFromDto(PostCreateRequest dto) {
        if (dto.getAuthorId() == null) return Optional.empty();
        Optional<User> userOpt = userRepository.findById(dto.getAuthorId());
        if (userOpt.isEmpty()) return Optional.empty();

        Post p = new Post();
        p.setAuthor(userOpt.get());
        p.setContent(dto.getContent());
        p.setVisibility(dto.getVisibility());
        p.setPostType(dto.getPostType());
        p.setStatus(dto.getStatus());

        Post saved = postRepository.save(p);

        if (dto.getMedia() != null) {
            for (PostCreateRequest.MediaItem m : dto.getMedia()) {
                if (m.getMediaType() == null || m.getMediaUrl() == null) continue;
                PostMedia pm = new PostMedia();
                pm.setPost(saved);
                pm.setMediaType(m.getMediaType());
                pm.setMediaUrl(m.getMediaUrl());
                postMediaRepository.save(pm);
            }
        }

        return Optional.of(saved);
    }

    public Post updatePost(Integer id, Post post) {
        Optional<Post> existing = postRepository.findById(id);
        if (existing.isEmpty()) return null;
        Post e = existing.get();
        // simple merge of fields (null-safe)
        if (post.getContent() != null) e.setContent(post.getContent());
        if (post.getVisibility() != null) e.setVisibility(post.getVisibility());
        if (post.getPostType() != null) e.setPostType(post.getPostType());
        if (post.getStatus() != null) e.setStatus(post.getStatus());
        return postRepository.save(e);
    }

    @Transactional
    public Optional<Post> updateFromDto(Integer id, PostUpdateRequest dto) {
        Optional<Post> existing = postRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();
        Post e = existing.get();

        if (dto.getContent() != null) e.setContent(dto.getContent());
        if (dto.getVisibility() != null) e.setVisibility(dto.getVisibility());
        if (dto.getPostType() != null) e.setPostType(dto.getPostType());
        if (dto.getStatus() != null) e.setStatus(dto.getStatus());

        Post saved = postRepository.save(e);

        // If media provided, replace existing media for the post with the provided list
        if (dto.getMedia() != null) {
            postMediaRepository.deleteByPostId(saved.getId());
            for (PostUpdateRequest.MediaItem m : dto.getMedia()) {
                if (m.getMediaType() == null || m.getMediaUrl() == null) continue;
                PostMedia pm = new PostMedia();
                pm.setPost(saved);
                pm.setMediaType(m.getMediaType());
                pm.setMediaUrl(m.getMediaUrl());
                postMediaRepository.save(pm);
            }
        }

        return Optional.of(saved);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }
}
