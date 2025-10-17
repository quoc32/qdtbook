package qdt.hcmute.vn.dqtbook_backend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qdt.hcmute.vn.dqtbook_backend.dto.ReportRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.model.Report;
import qdt.hcmute.vn.dqtbook_backend.service.ReportService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> reportPost(@PathVariable Integer postId,
                                        @RequestBody(required = false) ReportRequestDTO dto,
                                        HttpSession session) {
        if (dto == null || dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Reason is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
        Report r = reportService.createReportFor("POST", postId, dto, session);
        Map<String, Object> body = new HashMap<>();
        body.put("report_id", r.getId());
        body.put("reporter_id", r.getReporter() != null ? r.getReporter().getId() : null);
        body.put("reason", r.getReason());
        body.put("status", r.getStatus());
        body.put("created_at", r.getCreatedAt());
        body.put("reported_post_id", r.getReportedPostId());
        body.put("reported_comment_id", r.getReportedCommentId());
        body.put("reported_product_id", r.getReportedProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> reportComment(@PathVariable Integer commentId,
                                           @RequestBody(required = false) ReportRequestDTO dto,
                                           HttpSession session) {
        if (dto == null || dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Reason is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
        Report r = reportService.createReportFor("COMMENT", commentId, dto, session);
        Map<String, Object> body = new HashMap<>();
        body.put("report_id", r.getId());
        body.put("reporter_id", r.getReporter() != null ? r.getReporter().getId() : null);
        body.put("reason", r.getReason());
        body.put("status", r.getStatus());
        body.put("created_at", r.getCreatedAt());
        body.put("reported_post_id", r.getReportedPostId());
        body.put("reported_comment_id", r.getReportedCommentId());
        body.put("reported_product_id", r.getReportedProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/market/{marketItemId}")
    public ResponseEntity<?> reportMarketItem(@PathVariable Integer marketItemId,
                                              @RequestBody(required = false) ReportRequestDTO dto,
                                              HttpSession session) {
    if (dto == null || dto.getReason() == null || dto.getReason().trim().isEmpty()) {
        Map<String, Object> err = new HashMap<>();
        err.put("message", "Reason is required");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
    Report r = reportService.createReportFor("PRODUCT", marketItemId, dto, session);
        Map<String, Object> body = new HashMap<>();
        body.put("report_id", r.getId());
        body.put("reporter_id", r.getReporter() != null ? r.getReporter().getId() : null);
        body.put("reason", r.getReason());
        body.put("status", r.getStatus());
        body.put("created_at", r.getCreatedAt());
        body.put("reported_post_id", r.getReportedPostId());
        body.put("reported_comment_id", r.getReportedCommentId());
        body.put("reported_product_id", r.getReportedProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<?> listReports() {
        return ResponseEntity.ok(reportService.listAllReports());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Integer id, HttpSession session) {
        reportService.deleteReport(id, session);
        return ResponseEntity.noContent().build();
    }
}
