package org.example.fitvision.email.dto;

public record EmailRequest(String recipientEmail, String subject, EmailTemplate emailTemplate) {}
