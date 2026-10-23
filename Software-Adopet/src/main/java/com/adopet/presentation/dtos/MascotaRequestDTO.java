package com.adopet.presentation.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MascotaRequestDTO(
        @NotBlank String nombre,
        @NotBlank String especie,
        String raza,
        @NotBlank String sexo,
        @NotNull @Min(0) Integer edad,
        String tamano,
        String descripcion,
        @NotBlank String estadoSalud,
        String imagenUrl) {
}