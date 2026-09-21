package com.adopet.business.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import com.adopet.business.exceptions.DatosMascotaInvalidosException;
import com.adopet.business.exceptions.MascotaNoEncontradaException;
import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.Mascota;
import com.adopet.data.dao.MascotaRepository;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    public Mascota registrarMascota(Mascota mascota) {
        validarDatosObligatorios(mascota);
        mascota.setEstado(EstadoMascota.DISPONIBLE);
        return mascotaRepository.save(mascota);
    }

    public Mascota actualizarMascota(Long id, Mascota datosActualizados) {
        validarDatosObligatorios(datosActualizados);

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNoEncontradaException(id));

        mascota.setNombre(datosActualizados.getNombre());
        mascota.setEspecie(datosActualizados.getEspecie());
        mascota.setRaza(datosActualizados.getRaza());
        mascota.setSexo(datosActualizados.getSexo());
        mascota.setEdad(datosActualizados.getEdad());
        mascota.setTamano(datosActualizados.getTamano());
        mascota.setDescripcion(datosActualizados.getDescripcion());
        mascota.setEstadoSalud(datosActualizados.getEstadoSalud());
        mascota.setImagenUrl(datosActualizados.getImagenUrl());

        return mascotaRepository.save(mascota);
    }

    public Mascota desactivarMascota(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNoEncontradaException(id));

        mascota.setEstado(EstadoMascota.NO_DISPONIBLE);
        return mascotaRepository.save(mascota);
    }

    public List<Mascota> listarDisponibles() {
        return mascotaRepository.findByEstado(EstadoMascota.DISPONIBLE);
    }

    public List<Mascota> buscarYFiltrar(
            String especie,
            String raza,
            String sexo,
            Integer edadMin,
            Integer edadMax,
            String tamano) {
        Specification<Mascota> specification = Specification
                .where((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get("estado"), EstadoMascota.DISPONIBLE));

        if (especie != null && !especie.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("especie"), especie));
        }
        if (raza != null && !raza.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("raza"), raza));
        }
        if (sexo != null && !sexo.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("sexo"), sexo));
        }
        if (edadMin != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                    root.get("edad"), edadMin));
        }
        if (edadMax != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                    root.get("edad"), edadMax));
        }
        if (tamano != null && !tamano.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("tamano"), tamano));
        }

        return mascotaRepository.findAll(specification);
    }

    public Mascota obtenerDetalle(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNoEncontradaException(id));

        if (mascota.getEstado() != EstadoMascota.DISPONIBLE) {
            throw new MascotaNoEncontradaException(id);
        }

        return mascota;
    }

    public Mascota obtenerDetalleAdministrador(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNoEncontradaException(id));
    }

    private void validarDatosObligatorios(Mascota mascota) {
        if (mascota == null) {
            throw new DatosMascotaInvalidosException("Los datos de la mascota son obligatorios");
        }
        if (esVacio(mascota.getNombre())) {
            throw new DatosMascotaInvalidosException("El nombre de la mascota es obligatorio");
        }
        if (esVacio(mascota.getEspecie())) {
            throw new DatosMascotaInvalidosException("La especie de la mascota es obligatoria");
        }
        if (esVacio(mascota.getSexo())) {
            throw new DatosMascotaInvalidosException("El sexo de la mascota es obligatorio");
        }
        if (mascota.getEdad() == null || mascota.getEdad() < 0) {
            throw new DatosMascotaInvalidosException("La edad de la mascota debe ser válida");
        }
        if (esVacio(mascota.getEstadoSalud())) {
            throw new DatosMascotaInvalidosException("El estado de salud es obligatorio");
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}