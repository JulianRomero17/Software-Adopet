package com.adopet.business.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.adopet.business.exceptions.AccesoNoAutorizadoException;
import com.adopet.business.exceptions.SolicitudNoEncontradaException;
import com.adopet.business.exceptions.SolicitudAccesoDenegadoException;
import com.adopet.business.models.Seguimiento;
import com.adopet.business.models.SolicitudAdopcion;
import com.adopet.data.dao.SeguimientoRepository;
import com.adopet.data.dao.SolicitudAdopcionRepository;

@Service
public class SeguimientoService {

    private final SeguimientoRepository seguimientoRepository;
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;

    public SeguimientoService(
            SeguimientoRepository seguimientoRepository,
            SolicitudAdopcionRepository solicitudAdopcionRepository) {
        this.seguimientoRepository = seguimientoRepository;
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
    }

    public Seguimiento registrarSeguimiento(Long solicitudId, String estado, String observaciones) {
        SolicitudAdopcion solicitud = obtenerSolicitud(solicitudId);
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setSolicitudAdopcion(solicitud);
        seguimiento.setFecha(LocalDate.now());
        seguimiento.setEstado(estado);
        seguimiento.setObservaciones(observaciones);
        return seguimientoRepository.save(seguimiento);
    }

    public List<Seguimiento> listarSeguimientoPorSolicitud(Long solicitudId) {
        return seguimientoRepository.findBySolicitudAdopcion_Id(solicitudId);
    }

    public List<Seguimiento> listarSeguimientoPorAdoptante(Long adoptanteId, Long solicitudId) {
        SolicitudAdopcion solicitud = obtenerSolicitud(solicitudId);
        if (!solicitud.getAdoptante().getId().equals(adoptanteId)) {
            throw new AccesoNoAutorizadoException(solicitudId);
        }
        return listarSeguimientoPorSolicitud(solicitudId);
    }

    public List<Seguimiento> listarSeguimientoAutorizado(
            Long solicitudId, Long adoptanteId, boolean administrador) {
        SolicitudAdopcion solicitud = obtenerSolicitud(solicitudId);
        if (!administrador && !solicitud.getAdoptante().getId().equals(adoptanteId)) {
            throw new SolicitudAccesoDenegadoException(solicitudId);
        }
        return listarSeguimientoPorSolicitud(solicitudId);
    }

    private SolicitudAdopcion obtenerSolicitud(Long solicitudId) {
        return solicitudAdopcionRepository.findById(solicitudId)
                .orElseThrow(() -> new SolicitudNoEncontradaException(solicitudId));
    }
}