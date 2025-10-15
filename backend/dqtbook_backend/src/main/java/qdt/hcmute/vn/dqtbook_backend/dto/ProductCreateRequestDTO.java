package qdt.hcmute.vn.dqtbook_backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProductCreateRequestDTO {
    private String productName;
    private String description;
    private Double price;
    private Integer quantity;
    private String address;
    private String status; // available, out_of_stock, hidden (optional on create)
    private List<String> mediaUrls; // optional list of media URLs already uploaded
}
