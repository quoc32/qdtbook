package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import qdt.hcmute.vn.dqtbook_backend.repository.NotificationRepository;
import qdt.hcmute.vn.dqtbook_backend.model.Notification;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotificationsByRecipientId(Integer userId) {
        return notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(userId);
    }

    public void deleteNotification(Integer notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Notification not found"
            );
        }
        notificationRepository.deleteById(notificationId);
    }
    
}
