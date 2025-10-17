package qdt.hcmute.vn.dqtbook_backend.dto;

import java.time.Instant;
import java.util.List;

/**
 * DTO for group chat response
 * Contains chat information and member details
 */
public class GroupChatResponseDTO {
    private Integer chatId;
    private String chatName;
    private String chatAvatarUrl;
    private Boolean isGroup;
    private Integer memberCount;
    private List<Integer> memberIds;
    private Instant createdAt;
    private Boolean isNewlyCreated;  // true nếu vừa tạo mới, false nếu đã tồn tại

    // Constructors
    public GroupChatResponseDTO() {
    }

    public GroupChatResponseDTO(Integer chatId, String chatName, String chatAvatarUrl, 
                               Boolean isGroup, Integer memberCount, List<Integer> memberIds,
                               Instant createdAt, Boolean isNewlyCreated) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.chatAvatarUrl = chatAvatarUrl;
        this.isGroup = isGroup;
        this.memberCount = memberCount;
        this.memberIds = memberIds;
        this.createdAt = createdAt;
        this.isNewlyCreated = isNewlyCreated;
    }

    // Getters and Setters
    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatAvatarUrl() {
        return chatAvatarUrl;
    }

    public void setChatAvatarUrl(String chatAvatarUrl) {
        this.chatAvatarUrl = chatAvatarUrl;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsNewlyCreated() {
        return isNewlyCreated;
    }

    public void setIsNewlyCreated(Boolean isNewlyCreated) {
        this.isNewlyCreated = isNewlyCreated;
    }

    @Override
    public String toString() {
        return "GroupChatResponseDTO{" +
                "chatId=" + chatId +
                ", chatName='" + chatName + '\'' +
                ", chatAvatarUrl='" + chatAvatarUrl + '\'' +
                ", isGroup=" + isGroup +
                ", memberCount=" + memberCount +
                ", memberIds=" + memberIds +
                ", createdAt=" + createdAt +
                ", isNewlyCreated=" + isNewlyCreated +
                '}';
    }
}
