package qdt.hcmute.vn.dqtbook_backend.dto;

public class DirectChatRequestDTO {
    private Integer userId1;
    private Integer userId2;
    private String chatName;

    // Constructors
    public DirectChatRequestDTO() {
    }

    public DirectChatRequestDTO(Integer userId1, Integer userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public DirectChatRequestDTO(Integer userId1, Integer userId2, String chatName) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.chatName = chatName;
    }

    // Getters and Setters
    public Integer getUserId1() {
        return userId1;
    }

    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }

    public Integer getUserId2() {
        return userId2;
    }

    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    @Override
    public String toString() {
        return "DirectChatRequestDTO{" +
                "userId1=" + userId1 +
                ", userId2=" + userId2 +
                ", chatName='" + chatName + '\'' +
                '}';
    }
}