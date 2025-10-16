package qdt.hcmute.vn.dqtbook_backend.dto;

public class CheckChatResponseDTO {
    private boolean exists;
    private Integer chatId;
    private String message;

    // Constructors
    public CheckChatResponseDTO() {
    }

    public CheckChatResponseDTO(boolean exists, Integer chatId, String message) {
        this.exists = exists;
        this.chatId = chatId;
        this.message = message;
    }

    // Getters and Setters
    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CheckChatResponseDTO{" +
                "exists=" + exists +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                '}';
    }
}