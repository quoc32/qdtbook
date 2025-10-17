package qdt.hcmute.vn.dqtbook_backend.dto;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for listing group chats
 */
public class GroupChatListResponseDTO {
    private Integer chatId;
    private String chatName;
    private String chatAvatarUrl;
    private Integer memberCount;
    private List<Integer> memberIds;
    private Instant createdAt;

    // Constructors
    public GroupChatListResponseDTO() {
    }

    public GroupChatListResponseDTO(Integer chatId, String chatName, String chatAvatarUrl, 
                                     Integer memberCount, List<Integer> memberIds, Instant createdAt) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.chatAvatarUrl = chatAvatarUrl;
        this.memberCount = memberCount;
        this.memberIds = memberIds;
        this.createdAt = createdAt;
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
}
