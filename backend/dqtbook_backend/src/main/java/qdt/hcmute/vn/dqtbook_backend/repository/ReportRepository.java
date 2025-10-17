package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qdt.hcmute.vn.dqtbook_backend.model.Report;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {
	Optional<Report> findByReporterIdAndReportedPostId(Integer reporterId, Integer reportedPostId);
	Optional<Report> findByReporterIdAndReportedCommentId(Integer reporterId, Integer reportedCommentId);
	Optional<Report> findByReporterIdAndReportedProductId(Integer reporterId, Integer reportedProductId);
}


