package com.adopet.presentation.dtos;

import com.adopet.business.models.RolUsuario;

public record LoginResponse(String token, RolUsuario rol) {
}