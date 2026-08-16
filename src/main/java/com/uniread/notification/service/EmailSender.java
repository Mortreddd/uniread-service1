package com.uniread.notification.service;

import com.uniread.notification.domain.entities.NotificationChannel;
import com.uniread.notification.domain.entities.NotificationMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender implements NotificationSender {

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    @Retryable(
            retryFor = {MailSendException.class, NullPointerException.class},
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    @Override
    public void send(NotificationMessage message) {
        try {

            message.getVariables().forEach((key, value) -> {
                log.debug("Key: {}, Value: {}", key, value);
            });
            String email = message.getVariables().get("email").toString();
            String subject = message.getType().getDefaultTitle();
            String content = templateService.render(message.getType(), message.getVariables());
            var emailMessage = buildEmailMessage(email, subject, content);

            send(emailMessage);
        } catch (MailSendException | NullPointerException exception) {
            log.error("Error occurred while send email: {}", exception.getMessage());
        }
    }

    @Recover
    public void recoverAfterRetries(MailSendException ex, NotificationMessage notificationMessage) {
        // Fallback logic after all retries are exhausted
        var email = notificationMessage.getVariables().get("email").toString();
        log.error("Failed to send email to {} after all retries: {}", email, ex.getMessage());
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    private MimeMessage buildEmailMessage(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            return message;
        } catch (MessagingException exception) {
            log.error("Error occurred while building email message content {}", content);
        }

        return null;
    }

    private void send(MimeMessage message) {
        mailSender.send(message);
        log.info("Email has been sent: {}", message);
    }
}
