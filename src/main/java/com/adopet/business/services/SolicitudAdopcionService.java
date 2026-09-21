package com.adopet.business.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.adopet.business.exceptions.AdoptanteNoEncontradoException;
import com.adopet.business.exceptions.MascotaNoDisponibleException;
import com.adopet.business.exceptions.MascotaNoEncontradaException;
import com.adopet.business.exceptions.SolicitudDuplicadaException;
import com.adopet.business.exceptions.SolicitudNoEncontradaException;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.EstadoSolicitud;
import com.adopet.business.models.Mascota;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.SolicitudAdopcion;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.MascotaRepository;
import com.adopet.data.dao.SolicitudAdopcionRepository;
import com.adopet.data.dao.UsuarioRepository;

@Service
public class SolicitudAdopcionService {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeguimientoService seguimientoService;
    private final NotificacionService notificacionService;

    public SolicitudAdopcionService(
            SolicitudAdopcionRepository solicitudAdopcionRepository,
            MascotaRepository mascotaRepository,
            UsuarioRepository usuarioRepository,
            SeguimientoService seguimientoService,
            NotificacionService notificacionService) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
        this.seguimientoService = seguimientoService;
        this.notificacionService = notificacionService;
    }

    public SolicitudAdopcion crearSolicitud(Long adoptanteId, Long mascotaId) {
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new MascotaNoEncontradaException(mascotaId));
        if (mascota.getEstado() != EstadoMascota.DISPONIBLE) {
            throw new MascotaNoDisponibleException(mascotaId);
        }

        if (solicitudAdopcionRepository.existsByAdoptante_IdAndMascota_IdAndEstado(
                adoptanteId, mascotaId, EstadoSolicitud.PENDIENTE)) {
            throw new SolicitudDuplicadaException(adoptanteId, mascotaId);
        }

        Adoptante adoptante = obtenerAdoptante(adoptanteId);
        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);
        solicitud.setFecha(LocalDate.now());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        return solicitudAdopcionRepository.save(solicitud);
    }

    public List<SolicitudAdopcion> listarSolicitudesPorAdoptante(Long adoptanteId) {
        return solicitudAdopcionRepository.findByAdoptante_Id(adoptanteId);
    }

    public List<SolicitudAdopcion> listarSolicitudesPendientes() {
        return solicitudAdopcionRepository.findByEstadoIn(
                List.of(EstadoSolicitud.PENDIENTE, EstadoSolicitud.EN_REVISION));
    }

    public List<SolicitudAdopcion> listarHistorial(Long usuarioId, RolUsuario rol) {
        if (rol == RolUsuario.ADOPTANTE) {
            return solicitudAdopcionRepository.findByAdoptante_IdAndEstadoIn(
                    usuarioId, List.of(
                        EstadoSolicitud.FINALIZADA,
                        EstadoSolicitud.RECHAZADA,
                        EstadoSolicitud.APROBADA));
        }
        if (rol == RolUsuario.ADMINISTRADOR) {
            return solicitudAdopcionRepository.findByEstadoIn(List.of(EstadoSolicitud.FINALIZADA));
        }
        throw new IllegalArgumentException("El rol del usuario no es válido");
    }

    @Transactional
    public SolicitudAdopcion decidirSolicitud(
            Long solicitudId,
            EstadoSolicitud decision,
            String observaciones) {
        if (decision != EstadoSolicitud.APROBADA && decision != EstadoSolicitud.RECHAZADA) {
            throw new IllegalArgumentException("La decisión debe ser APROBADA o RECHAZADA");
        }

        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(solicitudId)
                .orElseThrow(() -> new SolicitudNoEncontradaException(solicitudId));
        solicitud.setEstado(decision);
        solicitud.setObservaciones(observaciones);
        solicitud.setFechaActualizacion(LocalDateTime.now());

        if (decision == EstadoSolicitud.APROBADA) {
            Mascota mascota = solicitud.getMascota();
            mascota.setEstado(EstadoMascota.ADOPTADA);
            mascotaRepository.save(mascota);
            seguimientoService.registrarSeguimiento(solicitudId, "INICIAL", observaciones);
        }

        SolicitudAdopcion solicitudGuardada = solicitudAdopcionRepository.save(solicitud);
        notificacionService.notificarCambioEstado(solicitudGuardada);
        return solicitudGuardada;
    }

    private Adoptante obtenerAdoptante(Long adoptanteId) {
        Usuario usuario = usuarioRepository.findById(adoptanteId)
                .orElseThrow(() -> new AdoptanteNoEncontradoException(adoptanteId));
        if (!(usuario instanceof Adoptante adoptante)) {
            throw new AdoptanteNoEncontradoException(adoptanteId);
        }
        return adoptante;
    }
}