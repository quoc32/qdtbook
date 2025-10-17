package qdt.hcmute.vn.dqtbook_backend.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentResponseDTO {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private Integer parentCommentId;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
