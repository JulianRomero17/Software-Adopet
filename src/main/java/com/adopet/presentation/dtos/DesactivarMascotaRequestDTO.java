package com.adopet.presentation.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DesactivarMascotaRequestDTO(
        @NotBlank @Size(max = 1000) String motivo) {
}
