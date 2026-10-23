package com.adopet.presentation.dtos;

import com.adopet.business.models.EstadoSolicitud;

import jakarta.validation.constraints.NotNull;

public record DecidirSolicitudRequestDTO(
        @NotNull EstadoSolicitud decision,
        String observaciones) {
}