package com.learning.backend.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WelcomeEmailService {

    private static final Logger log = LoggerFactory.getLogger(WelcomeEmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@localhost}")
    private String from;

    public WelcomeEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeAsync(String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(username + "@users.local");
            message.setSubject("Welcome");
            message.setText("Your account " + username + " is ready.");
            mailSender.send(message);
        } catch (Exception ex) {
            log.warn("Welcome email failed for {}: {}", username, ex.getMessage());
        }
    }
}
