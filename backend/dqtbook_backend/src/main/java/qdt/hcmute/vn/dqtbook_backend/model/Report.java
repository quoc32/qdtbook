package qdt.hcmute.vn.dqtbook_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    // store specific foreign key depending on type
    @Column(name = "reported_post_id")
    private Integer reportedPostId;

    @Column(name = "reported_comment_id")
    private Integer reportedCommentId;

    @Column(name = "reported_product_id")
    private Integer reportedProductId;

    @Lob
    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;
}


