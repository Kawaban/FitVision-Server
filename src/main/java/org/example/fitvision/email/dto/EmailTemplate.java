package org.example.fitvision.email.dto;

import java.util.Map;

public record EmailTemplate(String templateName, Map<String, Object> templateModel) {}
