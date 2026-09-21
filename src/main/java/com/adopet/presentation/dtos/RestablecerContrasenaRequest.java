package com.adopet.presentation.dtos;

import jakarta.validation.constraints.NotBlank;

public record RestablecerContrasenaRequest(
        @NotBlank String token,
        @NotBlank String nuevaContrasena) {
}