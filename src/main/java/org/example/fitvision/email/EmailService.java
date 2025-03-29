package org.example.fitvision.email;

import jakarta.mail.MessagingException;
import org.example.fitvision.email.dto.EmailRequest;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendMail(EmailRequest emailRequest) throws MessagingException;
}
