package com.adopet.presentation.dtos;

import jakarta.validation.constraints.NotNull;

public record CrearSolicitudRequestDTO(
        @NotNull Long mascotaId,
        String tipoVivienda,
        String permisoMascotas,
        String experiencia,
        String tiempoDisponible,
        String motivacion) {
}