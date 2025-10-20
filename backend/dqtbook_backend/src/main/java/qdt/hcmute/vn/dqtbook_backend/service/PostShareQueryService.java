package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import qdt.hcmute.vn.dqtbook_backend.repository.PostShareRepository;
import qdt.hcmute.vn.dqtbook_backend.dto.PostShareResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.PostContentResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.model.PostShare;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostShareQueryService {
    private final PostShareRepository postShareRepository;
    private final PostService postService;

    public PostShareQueryService(PostShareRepository postShareRepository, PostService postService) {
        this.postShareRepository = postShareRepository;
        this.postService = postService;
    }

    public List<PostShareResponseDTO> listSharesByUser(Integer userId) {
        List<PostShare> shares = postShareRepository.findByUserId(userId);
        return shares.stream().map(s -> {
            PostShareResponseDTO dto = new PostShareResponseDTO();
            dto.setId(s.getId());
            dto.setUserId(s.getUser() != null ? s.getUser().getId() : null);
            // populate minimal user info
            if (s.getUser() != null) {
                PostContentResponseDTO.AuthorDTO u = new PostContentResponseDTO.AuthorDTO();
                u.setId(s.getUser().getId());
                u.setFullName(s.getUser().getFullName());
                u.setAvatarUrl(s.getUser().getAvatarUrl());
                dto.setUser(u);
            }
            dto.setSharedText(s.getSharedText());
            dto.setVisibility(s.getVisibility());
            dto.setCreatedAt(s.getCreatedAt());
            dto.setUpdatedAt(s.getUpdatedAt());
            // embed original post as PostContentResponseDTO
            dto.setOriginalPost(postService.mapToContentDto(s.getPost()));
            return dto;
        }).collect(Collectors.toList());
    }
}
