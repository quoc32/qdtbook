package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qdt.hcmute.vn.dqtbook_backend.model.ProductMedia;

import java.util.List;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, Integer> {
    List<ProductMedia> findByProduct_Id(Integer productId);
    void deleteByProduct_Id(Integer productId);
}
