package com.adopet.presentation.dtos;

import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.Mascota;

public record MascotaResponseDTO(
        Long id,
        String nombre,
        String especie,
        String raza,
        String sexo,
        Integer edad,
        String tamano,
        String descripcion,
        String estadoSalud,
        String imagenUrl,
        EstadoMascota estado) {

    public static MascotaResponseDTO desde(Mascota mascota) {
        return new MascotaResponseDTO(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getSexo(),
                mascota.getEdad(),
                mascota.getTamano(),
                mascota.getDescripcion(),
                mascota.getEstadoSalud(),
                mascota.getImagenUrl(),
                mascota.getEstado());
    }
}