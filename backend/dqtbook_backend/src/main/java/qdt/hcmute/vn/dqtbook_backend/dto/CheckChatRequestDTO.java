package qdt.hcmute.vn.dqtbook_backend.dto;

public class CheckChatRequestDTO {
    private Integer userId1;
    private Integer userId2;

    // Constructors
    public CheckChatRequestDTO() {
    }

    public CheckChatRequestDTO(Integer userId1, Integer userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
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

    @Override
    public String toString() {
        return "CheckChatRequestDTO{" +
                "userId1=" + userId1 +
                ", userId2=" + userId2 +
                '}';
    }
}