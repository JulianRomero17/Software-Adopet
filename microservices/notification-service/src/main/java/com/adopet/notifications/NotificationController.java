package com.adopet.notifications;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

@RestController
@RequestMapping
public class NotificationController {
    private final JavaMailSender mailSender;
    private final String sender;
    private final String internalApiKey;

    public NotificationController(JavaMailSender mailSender,
            @Value("${spring.mail.username}") String sender,
            @Value("${internal.api-key}") String internalApiKey) {
        this.mailSender = mailSender;
        this.sender = sender;
        this.internalApiKey = internalApiKey;
    }

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("service", "notification-service", "status", "UP"); }

    @PostMapping("/internal/notifications/status-change")
    public Map<String, String> sendStatusChange(@RequestHeader(value = "X-Internal-Api-Key", defaultValue = "") String suppliedKey,
            @Valid @RequestBody NotificationRequest request) {
        if (internalApiKey.isBlank() || !MessageDigest.isEqual(internalApiKey.getBytes(StandardCharsets.UTF_8), suppliedKey.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credencial interna inválida");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(request.destinatario());
        message.setSubject("Actualización de solicitud de adopción #" + request.solicitudId());
        message.setText("Su solicitud de adopción cambió al estado " + request.estado() + " el " + request.fechaCambio() + ".");
        mailSender.send(message);
        return Map.of("status", "sent", "solicitudId", request.solicitudId().toString());
    }
}
