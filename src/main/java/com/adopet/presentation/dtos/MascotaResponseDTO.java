package com.adopet.presentation.dtos;

import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.Mascota;

public record MascotaResponseDTO(
        Long id,
        String nombre,
        String edadTexto,
        String especie,
        String raza,
        String sexo,
        Integer edad,
        String tamano,
        String descripcion,
        String estadoSalud,
        String vacunacion,
        String esterilizacion,
        String imagenUrl,
        String motivoDesactivacion,
        EstadoMascota estado) {

    public static MascotaResponseDTO desde(Mascota mascota) {
        return new MascotaResponseDTO(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEdadTexto(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getSexo(),
                mascota.getEdad(),
                mascota.getTamano(),
                mascota.getDescripcion(),
                mascota.getEstadoSalud(),
                mascota.getVacunacion(),
                mascota.getEsterilizacion(),
                mascota.getImagenUrl(),
                mascota.getMotivoDesactivacion(),
                mascota.getEstado());
    }
}