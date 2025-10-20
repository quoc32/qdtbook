package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        System.out.println("✅ EmailService bean created successfully!");
    }

    public void sendSimpleEmail(String to, String subject, String text) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("leminhquoc0397144277@gmail.com");
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);
      mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom("leminhquoc0397144277@gmail.com");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true); // 'true' = nội dung HTML

      mailSender.send(message);
  }
}
