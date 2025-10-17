package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qdt.hcmute.vn.dqtbook_backend.model.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

  List<Notification> findAllByRecipientIdOrderByCreatedAtDesc(Integer userId);

  boolean existsById(Integer id);

  @Modifying
  @Transactional
  void deleteById(Integer id);

  @Transactional
  default Notification createNotification(Integer recipientId, String content, String type, Integer sourceId) {
    Notification notification = new Notification();
    notification.setRecipientId(recipientId);
    notification.setContent(content);
    notification.setType(type);
    notification.setSourceId(sourceId);
    return save(notification);
  }
}
