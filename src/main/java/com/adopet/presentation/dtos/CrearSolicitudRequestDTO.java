package com.adopet.presentation.dtos;

import jakarta.validation.constraints.NotNull;

public record CrearSolicitudRequestDTO(
        @NotNull Long mascotaId) {
}