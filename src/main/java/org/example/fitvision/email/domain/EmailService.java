package org.example.fitvision.email.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.val;
import org.example.fitvision.email.dto.EmailRequest;
import org.example.fitvision.email.dto.EmailTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
record EmailService(JavaMailSender mailSender, EmailProperties properties, TemplateEngine templateEngine)
        implements org.example.fitvision.email.EmailService {
    @Async
    @Override
    public void sendMail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom(properties.getUsername());
        helper.setSubject(emailRequest.subject());
        helper.setText(generateHtmlContent(emailRequest.emailTemplate()), true);
        helper.setTo(emailRequest.recipientEmail());
        mailSender.send(mimeMessage);
    }

    private String generateHtmlContent(EmailTemplate emailTemplate) {
        val context = new Context();
        context.setVariables(emailTemplate.templateModel());
        return templateEngine.process(emailTemplate.templateName(), context);
    }
}
