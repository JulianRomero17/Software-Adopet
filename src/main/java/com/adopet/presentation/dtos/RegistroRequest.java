package com.adopet.presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroRequest(
        @NotBlank String nombre,
        @NotBlank String documento,
        @NotBlank @Email String correo,
        String telefono,
        String direccion,
        @NotBlank String contrasena) {
}