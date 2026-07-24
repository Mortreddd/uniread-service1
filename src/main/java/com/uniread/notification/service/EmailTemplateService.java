package com.uniread.notification.service;

import com.uniread.notification.domain.entities.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String render(NotificationType type, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);

            String template = type.getDefaultTemplate();


            log.info("Rendering template: {}", template);

            return templateEngine.process(template, context);
        } catch (Exception e) {
            log.error("Failed to render template for type: {}", type, e);
            return "Please verify your email: " + variables.get("verificationLink");
        }
    }
}
