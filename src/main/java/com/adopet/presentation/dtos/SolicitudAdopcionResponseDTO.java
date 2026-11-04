package com.adopet.presentation.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.adopet.business.models.EstadoSolicitud;
import com.adopet.business.models.SolicitudAdopcion;

public record SolicitudAdopcionResponseDTO(
        Long id,
        LocalDate fecha,
        EstadoSolicitud estado,
        String observaciones,
        LocalDateTime fechaActualizacion,
        Long adoptanteId,
        Long mascotaId,
        Map<String, String> informacionAdopcion) {

    public static SolicitudAdopcionResponseDTO desde(SolicitudAdopcion solicitud) {
        return new SolicitudAdopcionResponseDTO(
                solicitud.getId(),
                solicitud.getFecha(),
                solicitud.getEstado(),
                solicitud.getObservaciones(),
                solicitud.getFechaActualizacion(),
                solicitud.getAdoptante().getId(),
                solicitud.getMascota().getId(),
                Map.of(
                        "tipoVivienda", valor(solicitud.getTipoVivienda()),
                        "permisoMascotas", valor(solicitud.getPermisoMascotas()),
                        "experiencia", valor(solicitud.getExperiencia()),
                        "tiempoDisponible", valor(solicitud.getTiempoDisponible()),
                        "motivacion", valor(solicitud.getMotivacion())));
    }

    private static String valor(String value) {
        return value == null ? "" : value;
    }
}