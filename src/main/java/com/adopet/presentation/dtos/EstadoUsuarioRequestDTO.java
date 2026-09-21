package com.adopet.presentation.dtos;

import jakarta.validation.constraints.NotNull;

public record EstadoUsuarioRequestDTO(
        @NotNull Boolean estado) {
}