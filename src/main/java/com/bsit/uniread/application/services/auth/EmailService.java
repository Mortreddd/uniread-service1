package com.bsit.uniread.application.services.auth;


import com.bsit.uniread.application.constants.EmailMessages;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${client.url}")
    private String clientUrl;


    /**
     * Sends an Email Confirmation Url for a forgot password request
     * @param email
     * @param otpId
     */
    public void sendForgotPasswordEmail(String email, UUID otpId) throws MessagingException {
        Context context = new Context();
        String forgotPasswordUrl = String.format("%s/auth/forgot-password?id=%s", clientUrl, otpId);
        context.setVariable("forgotPasswordVerificationUrl", forgotPasswordUrl);
        String fileName = "forgot-password";
        sendEmail(context, email, EmailMessages.VERIFY_FORGOT_PASSWORD_SUBJECT, fileName);
    }

    /**
     * Sends an Email Verification Url to a specific email
     * @param email
     * @param otpId
     * @throws MessagingException
     */
    public void sendVerificationUrlEmail(String email, UUID otpId) throws MessagingException {
        Context context = new Context();
        String verificationUrl = String.format("%s/auth/verify-email/%s", clientUrl, otpId);
        context.setVariable("verificationUrl", verificationUrl);
        String fileName = "email-verification";
        sendEmail(context, email, EmailMessages.VERIFY_EMAIL_SUBJECT, fileName);
    }

    /**
     * Sends an email using thymeleaf
     *
     * @param context
     * @param email
     * @param subject
     * @param fileName
     * @throws MessagingException
     */
    private void sendEmail(Context context, String email, String subject, String fileName) throws MessagingException {
        String process = templateEngine.process(fileName, context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(process, true);
        mailSender.send(mimeMessage);
    }
}
