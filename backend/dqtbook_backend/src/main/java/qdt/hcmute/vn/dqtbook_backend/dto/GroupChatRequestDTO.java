package qdt.hcmute.vn.dqtbook_backend.dto;

import java.util.List;

/**
 * DTO for creating or finding a group chat
 * Contains list of member IDs, chat name, and optional avatar
 */
public class GroupChatRequestDTO {
    private List<Integer> memberIds;  // Danh sách user IDs trong nhóm
    private String chatName;           // Tên nhóm (optional, có thể null)
    private String chatAvatarUrl;      // Avatar nhóm (optional)

    // Constructors
    public GroupChatRequestDTO() {
    }

    public GroupChatRequestDTO(List<Integer> memberIds, String chatName, String chatAvatarUrl) {
        this.memberIds = memberIds;
        this.chatName = chatName;
        this.chatAvatarUrl = chatAvatarUrl;
    }

    // Getters and Setters
    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
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

    @Override
    public String toString() {
        return "GroupChatRequestDTO{" +
                "memberIds=" + memberIds +
                ", chatName='" + chatName + '\'' +
                ", chatAvatarUrl='" + chatAvatarUrl + '\'' +
                '}';
    }
}
