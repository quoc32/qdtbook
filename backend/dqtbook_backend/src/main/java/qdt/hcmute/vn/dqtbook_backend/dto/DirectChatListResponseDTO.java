package qdt.hcmute.vn.dqtbook_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class DirectChatListResponseDTO {
    private Integer chatId;
    private String chatName;
    private Integer otherUserId;
    private String otherUserName;
    private String otherUserAvatar;
    private Boolean isOnline;
    private Instant createdAt;
}
