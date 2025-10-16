package qdt.hcmute.vn.dqtbook_backend.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.dto.*;
import qdt.hcmute.vn.dqtbook_backend.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/market/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final HttpSession session;

    private Integer currentUserId() {
        Object uid = session.getAttribute("userId");
        if (uid == null) throw new RuntimeException("Not authenticated");
        return (Integer) uid;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductCreateRequestDTO dto) {
        return ResponseEntity.ok(productService.create(currentUserId(), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Integer id, @RequestBody ProductUpdateRequestDTO dto) {
        return ResponseEntity.ok(productService.update(currentUserId(), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        productService.delete(currentUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<java.util.List<ProductResponseDTO>> list(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "seller_id", required = false) Integer sellerId,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "min_price", required = false) Double minPrice,
            @RequestParam(value = "max_price", required = false) Double maxPrice
    ) {
        return ResponseEntity.ok(productService.list(status, sellerId, q, minPrice, maxPrice));
    }


    // Danh sách sản phẩm của chính mình (tiện cho frontend gọi /seller/me)
    @GetMapping("/seller/me")
    public ResponseEntity<java.util.List<ProductResponseDTO>> listByMe() {
        return ResponseEntity.ok(productService.list(null, currentUserId(), null, null, null));
    }

    // Danh sách sản phẩm public (tất cả người bán) - mặc định chỉ lấy status=available
    @GetMapping("/public")
    public ResponseEntity<java.util.List<ProductResponseDTO>> listPublic(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "min_price", required = false) Double minPrice,
            @RequestParam(value = "max_price", required = false) Double maxPrice
    ) {
        String effectiveStatus = (status == null || status.isBlank()) ? "available" : status;
        System.out.println("[Market] listPublic status=" + effectiveStatus + ", q=" + q + ", min=" + minPrice + ", max=" + maxPrice);
        var result = productService.list(effectiveStatus, null, q, minPrice, maxPrice);
        System.out.println("[Market] listPublic result size=" + (result != null ? result.size() : -1));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> get(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<?> share(@PathVariable Integer id, @RequestBody ProductShareRequestDTO dto) {
        var list = productService.share(currentUserId(), id, dto);
        return ResponseEntity.ok(Map.of("recipients", list));
    }

}
