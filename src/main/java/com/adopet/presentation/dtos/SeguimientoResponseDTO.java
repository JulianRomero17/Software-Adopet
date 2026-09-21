package com.adopet.presentation.dtos;

import java.time.LocalDate;

import com.adopet.business.models.Seguimiento;

public record SeguimientoResponseDTO(
        Long id,
        LocalDate fecha,
        String estado,
        String observaciones,
        Long solicitudId) {

    public static SeguimientoResponseDTO desde(Seguimiento seguimiento) {
        return new SeguimientoResponseDTO(
                seguimiento.getId(),
                seguimiento.getFecha(),
                seguimiento.getEstado(),
                seguimiento.getObservaciones(),
                seguimiento.getSolicitudAdopcion().getId());
    }
}