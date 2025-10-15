package qdt.hcmute.vn.dqtbook_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponseDTO {
    private Integer id;
    private Integer sellerId;
    private String sellerName;
    private String productName;
    private String description;
    private Double price;
    private Integer quantity;
    private String status;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> mediaUrls;
}
