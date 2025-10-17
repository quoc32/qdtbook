package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qdt.hcmute.vn.dqtbook_backend.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import qdt.hcmute.vn.dqtbook_backend.model.Notification;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping("/{userId}/all")
  public ResponseEntity<?> getAllNotifications(
    @PathVariable("userId") Integer userId) {

    List<Notification> notifications = notificationService.getAllNotificationsByRecipientId(userId);

    return ResponseEntity.ok(notifications);
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotification(
    @PathVariable("notificationId") Integer notificationId) {

    notificationService.deleteNotification(notificationId);
    return ResponseEntity.noContent().build();
  }
}
