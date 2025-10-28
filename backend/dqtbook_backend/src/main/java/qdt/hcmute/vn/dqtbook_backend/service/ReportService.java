package qdt.hcmute.vn.dqtbook_backend.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import qdt.hcmute.vn.dqtbook_backend.dto.ReportRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.model.Report;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.ReportRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Report createReportFor(String contentType, Integer contentId, ReportRequestDTO dto, HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }

        Integer userId;
        try {
            userId = Integer.parseInt(String.valueOf(userIdObj));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session user id");
        }

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        User reporter = optUser.get();

        // try to find existing report by this reporter for the same target
        Optional<Report> existing = Optional.empty();
        if ("POST".equalsIgnoreCase(contentType)) {
            existing = reportRepository.findByReporterIdAndReportedPostId(reporter.getId(), contentId);
        } else if ("COMMENT".equalsIgnoreCase(contentType)) {
            existing = reportRepository.findByReporterIdAndReportedCommentId(reporter.getId(), contentId);
        } else if ("PRODUCT".equalsIgnoreCase(contentType)) {
            existing = reportRepository.findByReporterIdAndReportedProductId(reporter.getId(), contentId);
        } else if ("SHARE".equalsIgnoreCase(contentType)) {
            existing = reportRepository.findByReporterIdAndReportedShareId(reporter.getId(), contentId);
        }

        if (existing.isPresent()) {
            Report r = existing.get();
            r.setReason(dto != null ? dto.getReason() : r.getReason());
            // update status according to type
            if ("COMMENT".equalsIgnoreCase(contentType)) {
                r.setStatus("COMMENT");
            } else if ("POST".equalsIgnoreCase(contentType)) {
                r.setStatus("POST");
            } else if ("PRODUCT".equalsIgnoreCase(contentType)) {
                r.setStatus("PRODUCT");
            } else if ("SHARE".equalsIgnoreCase(contentType)) {
                r.setStatus("SHARE");
            }
            r.setCreatedAt(Instant.now());
            return reportRepository.save(r);
        }

        Report report = new Report();
        report.setReporter(reporter);
        // set only the appropriate reported_* id according to type
        if ("POST".equalsIgnoreCase(contentType)) {
            report.setReportedPostId(contentId);
        } else if ("COMMENT".equalsIgnoreCase(contentType)) {
            report.setReportedCommentId(contentId);
        } else if ("PRODUCT".equalsIgnoreCase(contentType)) {
            report.setReportedProductId(contentId);
        } else if ("SHARE".equalsIgnoreCase(contentType)) {
            report.setReportedShareId(contentId);
        }
        report.setReason(dto != null ? dto.getReason() : null);
        // set status based on content type as requested: COMMENT, POST, PRODUCT
        if ("COMMENT".equalsIgnoreCase(contentType)) {
            report.setStatus("COMMENT");
        } else if ("POST".equalsIgnoreCase(contentType)) {
            report.setStatus("POST");
        } else if ("PRODUCT".equalsIgnoreCase(contentType)) {
            report.setStatus("PRODUCT");
        } else if ("SHARE".equalsIgnoreCase(contentType)) {
            report.setStatus("SHARE");
        } else {
            report.setStatus("NEW");
        }
        report.setCreatedAt(Instant.now());

        return reportRepository.save(report);
    }

    public List<Report> listAllReports() {
        return reportRepository.findAll();
    }

    public void deleteReport(Integer reportId, HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }

        // simple delete - any logged-in user can delete. Extend with role checks if needed.
        reportRepository.deleteById(reportId);
    }
}
