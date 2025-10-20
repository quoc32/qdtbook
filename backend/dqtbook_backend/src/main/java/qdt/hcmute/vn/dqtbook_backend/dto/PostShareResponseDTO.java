package qdt.hcmute.vn.dqtbook_backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class PostShareResponseDTO {
    private Integer id;
    private Integer userId;
    // Sharer details (id, fullName, avatarUrl) to help frontend render the share card
    private qdt.hcmute.vn.dqtbook_backend.dto.PostContentResponseDTO.AuthorDTO user;
    private String sharedText;
    private String visibility;
    private Instant createdAt;
    private Instant updatedAt;
    // The original post embedded (use existing PostContentResponseDTO)
    private PostContentResponseDTO originalPost;
}
