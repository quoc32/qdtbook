package qdt.hcmute.vn.dqtbook_backend.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import qdt.hcmute.vn.dqtbook_backend.dto.*;
import qdt.hcmute.vn.dqtbook_backend.exception.ResourceNotFoundException;
import qdt.hcmute.vn.dqtbook_backend.model.Product;
import qdt.hcmute.vn.dqtbook_backend.model.ProductMedia;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.ProductMediaRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.ProductRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Transactional
    public ProductResponseDTO create(Integer sessionUserId, ProductCreateRequestDTO dto) {
        User seller = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + sessionUserId));
        Product p = new Product();
        p.setSeller(seller);
        p.setProductName(dto.getProductName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setQuantity(dto.getQuantity());
        p.setAddress(dto.getAddress());
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            p.setStatus(dto.getStatus());
        } else {
            p.setStatus("available");
        }
        List<String> mediaUrls = dto.getMediaUrls();
        if (mediaUrls != null) {
            for (String url : mediaUrls) {
                ProductMedia pm = new ProductMedia();
                pm.setProduct(p);
                pm.setMediaUrl(url);
                p.getMedias().add(pm); // cascade persist
            }
        }
        productRepository.save(p); // cascade sẽ lưu cả medias
        return toDTOWithMedia(p);
    }

    @Transactional
    public ProductResponseDTO update(Integer sessionUserId, Integer productId, ProductUpdateRequestDTO dto) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        if (!p.getSeller().getId().equals(sessionUserId)) {
            throw new RuntimeException("Forbidden: not product owner");
        }

        if (dto.getProductName() != null) p.setProductName(dto.getProductName());
        if (dto.getDescription() != null) p.setDescription(dto.getDescription());
        if (dto.getPrice() != null) p.setPrice(dto.getPrice());
        if (dto.getQuantity() != null) p.setQuantity(dto.getQuantity());
        if (dto.getAddress() != null) p.setAddress(dto.getAddress());
        if (dto.getStatus() != null) p.setStatus(dto.getStatus());
        p.setUpdatedAt(Instant.now());

        // Thay thế danh sách media bằng cách thao tác trực tiếp trên collection đã được Hibernate quản lý
        if (dto.getMediaUrls() != null) {
            List<ProductMedia> managedMedias = p.getMedias(); // luôn dùng collection này, không được set list mới
            // ép load nếu cần
            managedMedias.size();
            // Xóa hết (orphanRemoval sẽ tự xóa record cũ)
            managedMedias.clear();
            // Thêm lại danh sách mới
            for (String url : dto.getMediaUrls()) {
                ProductMedia pm = new ProductMedia();
                pm.setProduct(p);
                pm.setMediaUrl(url);
                managedMedias.add(pm);
            }
        }
        return toDTOWithMedia(p);
    }

    @Transactional
    public void delete(Integer sessionUserId, Integer productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        
        String role = (String) session.getAttribute("role");
        if (!p.getSeller().getId().equals(sessionUserId) && !"admin".equals(role)) {
            throw new RuntimeException("Forbidden: not product owner");
        }
        productRepository.delete(p);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO get(Integer productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        // lazy medias may not be loaded; ensure fetch
        List<String> mediaUrls = productMediaRepository.findByProduct_Id(p.getId())
                .stream().map(ProductMedia::getMediaUrl).toList();
        ProductResponseDTO dto = toDTO(p);
        dto.setMediaUrls(mediaUrls);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> list(String status, Integer sellerId, String q, Double minPrice, Double maxPrice, String province, String district) {
        // Khởi tạo spec với predicate luôn đúng để tránh null khi .and()
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (sellerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("seller").get("id"), sellerId));
        }
        if (q != null && !q.isBlank()) {
            String like = ("%" + q.trim().toLowerCase() + "%");
            spec = spec.and((root, query, cb) -> {
                var nameExp = cb.lower(cb.coalesce(root.get("productName"), ""));
                return cb.like(nameExp, like);
            });
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        // Filter by province/district if provided. We store address as a composed string (detail, district, province)
        if (province != null && !province.isBlank()) {
            String p = "%" + province.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(cb.coalesce(root.get("address"), "")), p));
        }
        if (district != null && !district.isBlank()) {
            String d = "%" + district.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(cb.coalesce(root.get("address"), "")), d));
        }
        List<Product> data = productRepository.findAll(spec);
        return data.stream().map(this::toDTOWithMedia).toList();
    }


    public List<String> share(Integer sessionUserId, Integer productId, ProductShareRequestDTO dto) {
        // TODO: integrate NotificationRepository to create Notification rows of type 'product_share'
        // Example (pseudo):
        // Product product = productRepository.getReferenceById(productId);
        // for(Integer toId: dto.getToUserIds()) { create notification (recipient=toId, sender=sessionUserId, sourceId=productId, type='product_share') }
        return dto.getToUserIds().stream().map(String::valueOf).toList();
    }


    private ProductResponseDTO toDTO(Product p) {
        return ProductResponseDTO.builder()
                .id(p.getId())
                .sellerId(p.getSeller() != null ? p.getSeller().getId() : null)
                .sellerName(p.getSeller() != null ? p.getSeller().getFullName() : null)
                .productName(p.getProductName())
                .description(p.getDescription())
                .price(p.getPrice())
                .quantity(p.getQuantity())
                .status(p.getStatus())
                .address(p.getAddress())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private ProductResponseDTO toDTOWithMedia(Product p) {
        List<String> mediaUrls = productMediaRepository.findByProduct_Id(p.getId())
                .stream().map(ProductMedia::getMediaUrl).toList();
        ProductResponseDTO dto = toDTO(p);
        dto.setMediaUrls(mediaUrls);
        return dto;
    }
}
