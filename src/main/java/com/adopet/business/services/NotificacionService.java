package com.adopet.business.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adopet.business.models.SolicitudAdopcion;

@Service
public class NotificacionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionService.class);
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    private final String endpoint;
    private final String apiKey;

    public NotificacionService(
            @Value("${adopet.notifications.url:http://localhost:8081}") String baseUrl,
            @Value("${adopet.notifications.api-key:}") String apiKey) {
        this.endpoint = baseUrl.replaceAll("/$", "") + "/internal/notifications/status-change";
        this.apiKey = apiKey;
    }

    public void notificarCambioEstado(SolicitudAdopcion solicitud) {
        try {
            LocalDateTime fechaCambio = solicitud.getFechaActualizacion() != null
                    ? solicitud.getFechaActualizacion() : LocalDateTime.now();
            String json = "{\"solicitudId\":" + solicitud.getId()
                    + ",\"destinatario\":\"" + escapar(solicitud.getAdoptante().getCorreo())
                    + "\",\"estado\":\"" + solicitud.getEstado()
                    + "\",\"fechaCambio\":\"" + fechaCambio + "\"}";
            HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                    .timeout(Duration.ofSeconds(4)).header("Content-Type", "application/json")
                    .header("X-Internal-Api-Key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json)).build();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() >= 400) {
                LOGGER.error("El microservicio de notificaciones respondió HTTP {} para solicitud {}",
                        response.statusCode(), solicitud.getId());
            }
        } catch (Exception exception) {
            LOGGER.error("No se pudo solicitar la notificación de la solicitud {}", solicitud.getId(), exception);
        }
    }

    private String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
