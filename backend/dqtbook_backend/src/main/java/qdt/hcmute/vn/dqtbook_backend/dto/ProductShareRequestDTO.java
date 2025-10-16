package qdt.hcmute.vn.dqtbook_backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProductShareRequestDTO {
    private List<Integer> toUserIds;
    private String message;
}
