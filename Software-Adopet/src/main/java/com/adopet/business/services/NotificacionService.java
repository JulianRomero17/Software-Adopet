package com.adopet.business.services;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.adopet.business.models.SolicitudAdopcion;

@Service
public class NotificacionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionService.class);

    private final JavaMailSender mailSender;
    private final String remitente;

    public NotificacionService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    public void notificarCambioEstado(SolicitudAdopcion solicitud) {
        try {
            LocalDateTime fechaCambio = solicitud.getFechaActualizacion() != null
                    ? solicitud.getFechaActualizacion()
                    : LocalDateTime.now();

            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(solicitud.getAdoptante().getCorreo());
            mensaje.setSubject("Actualización de solicitud de adopción");
            mensaje.setText("Su solicitud de adopción cambió al estado "
                    + solicitud.getEstado() + " el " + fechaCambio + ".");
            mailSender.send(mensaje);
        } catch (RuntimeException exception) {
            LOGGER.error("No se pudo enviar la notificación de la solicitud {}",
                    solicitud.getId(), exception);
        }
    }
}