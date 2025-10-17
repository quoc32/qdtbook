package qdt.hcmute.vn.dqtbook_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Integer id;

    @JoinColumn(name = "recipient_id", nullable = false)
    private Integer recipientId;

    @JoinColumn(name = "sender_id")
    private Integer senderId;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "source_id")
    private Integer sourceId;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_at")
    private Instant createdAt;
}


