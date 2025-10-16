package qdt.hcmute.vn.dqtbook_backend.dto;

import java.time.Instant;

public class DirectChatResponseDTO {
    private Integer chatId;
    private Integer otherUserId;
    private String otherUserName;
    private String otherUserAvatar;
    private Boolean isOnline;
    private Instant createdAt;
    private String lastMessage;
    private Integer unreadCount;

    // Constructors
    public DirectChatResponseDTO() {
    }

    public DirectChatResponseDTO(Integer chatId, Integer otherUserId, String otherUserName,
            String otherUserAvatar, Boolean isOnline, Instant createdAt) {
        this.chatId = chatId;
        this.otherUserId = otherUserId;
        this.otherUserName = otherUserName;
        this.otherUserAvatar = otherUserAvatar;
        this.isOnline = isOnline;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Integer otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getOtherUserAvatar() {
        return otherUserAvatar;
    }

    public void setOtherUserAvatar(String otherUserAvatar) {
        this.otherUserAvatar = otherUserAvatar;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString() {
        return "DirectChatResponseDTO{" +
                "chatId=" + chatId +
                ", otherUserId=" + otherUserId +
                ", otherUserName='" + otherUserName + '\'' +
                ", otherUserAvatar='" + otherUserAvatar + '\'' +
                ", isOnline=" + isOnline +
                ", createdAt=" + createdAt +
                ", lastMessage='" + lastMessage + '\'' +
                ", unreadCount=" + unreadCount +
                '}';
    }
}