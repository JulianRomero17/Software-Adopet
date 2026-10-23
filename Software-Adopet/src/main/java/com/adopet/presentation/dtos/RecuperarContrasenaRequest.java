package com.adopet.presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarContrasenaRequest(
        @NotBlank @Email String correo) {
}