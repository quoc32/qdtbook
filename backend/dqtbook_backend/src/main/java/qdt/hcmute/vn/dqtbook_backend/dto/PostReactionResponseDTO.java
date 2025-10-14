package qdt.hcmute.vn.dqtbook_backend.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReactionResponseDTO {
    private Integer id;
    private String reactionType;
    private Instant createdAt;
    private Integer authorId;
    private Integer postId;
}
