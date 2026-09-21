package com.adopet.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adopet.business.exceptions.MascotaNoDisponibleException;
import com.adopet.business.exceptions.SolicitudDuplicadaException;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.EstadoSolicitud;
import com.adopet.business.models.Mascota;
import com.adopet.business.models.SolicitudAdopcion;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.MascotaRepository;
import com.adopet.data.dao.SolicitudAdopcionRepository;
import com.adopet.data.dao.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class SolicitudAdopcionServiceTest {

    @Mock
    private SolicitudAdopcionRepository solicitudAdopcionRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SeguimientoService seguimientoService;

    @Mock
    private NotificacionService notificacionService;

    private SolicitudAdopcionService solicitudService;

    @BeforeEach
    void setUp() {
        solicitudService = new SolicitudAdopcionService(
                solicitudAdopcionRepository,
                mascotaRepository,
                usuarioRepository,
                seguimientoService,
                notificacionService);
    }

    @Test
    void crearSolicitudDebeLanzarExcepcionSiMascotaNoEstaDisponible() {
        Long adoptanteId = 1L;
        Long mascotaId = 10L;
        Mascota mascota = mascota(mascotaId, EstadoMascota.ADOPTADA);
        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascota));

        assertThrows(
                MascotaNoDisponibleException.class,
                () -> solicitudService.crearSolicitud(adoptanteId, mascotaId));
    }

    @Test
    void crearSolicitudDebeLanzarExcepcionSiExisteSolicitudPendiente() {
        Long adoptanteId = 1L;
        Long mascotaId = 10L;
        Mascota mascota = mascota(mascotaId, EstadoMascota.DISPONIBLE);
        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascota));
        when(solicitudAdopcionRepository.existsByAdoptante_IdAndMascota_IdAndEstado(
                adoptanteId, mascotaId, EstadoSolicitud.PENDIENTE)).thenReturn(true);

        assertThrows(
                SolicitudDuplicadaException.class,
                () -> solicitudService.crearSolicitud(adoptanteId, mascotaId));
    }

    @Test
    void crearSolicitudDebeGuardarSolicitudPendienteCuandoEsValida() {
        Long adoptanteId = 1L;
        Long mascotaId = 10L;
        Adoptante adoptante = adoptante(adoptanteId);
        Mascota mascota = mascota(mascotaId, EstadoMascota.DISPONIBLE);
        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascota));
        when(solicitudAdopcionRepository.existsByAdoptante_IdAndMascota_IdAndEstado(
                adoptanteId, mascotaId, EstadoSolicitud.PENDIENTE)).thenReturn(false);
        when(usuarioRepository.findById(adoptanteId)).thenReturn(Optional.of(adoptante));

        solicitudService.crearSolicitud(adoptanteId, mascotaId);

        ArgumentCaptor<SolicitudAdopcion> captor = ArgumentCaptor.forClass(SolicitudAdopcion.class);
        verify(solicitudAdopcionRepository).save(captor.capture());
        SolicitudAdopcion guardada = captor.getValue();
        assertEquals(adoptante, guardada.getAdoptante());
        assertEquals(mascota, guardada.getMascota());
        assertEquals(EstadoSolicitud.PENDIENTE, guardada.getEstado());
    }

    @Test
    void decidirSolicitudAprobadaDebeAdoptarMascotaYCrearSeguimiento() {
        Long solicitudId = 20L;
        Mascota mascota = mascota(10L, EstadoMascota.DISPONIBLE);
        SolicitudAdopcion solicitud = solicitud(solicitudId, mascota, EstadoSolicitud.PENDIENTE);
        when(solicitudAdopcionRepository.findById(solicitudId)).thenReturn(Optional.of(solicitud));
        when(solicitudAdopcionRepository.save(solicitud)).thenReturn(solicitud);

        solicitudService.decidirSolicitud(solicitudId, EstadoSolicitud.APROBADA, "Aprobada");

        assertEquals(EstadoMascota.ADOPTADA, mascota.getEstado());
        verify(mascotaRepository).save(mascota);
        verify(seguimientoService).registrarSeguimiento(solicitudId, "INICIAL", "Aprobada");
        verify(solicitudAdopcionRepository).save(solicitud);
        verify(notificacionService).notificarCambioEstado(solicitud);
    }

    @Test
    void decidirSolicitudRechazadaNoDebeModificarEstadoDeMascota() {
        Long solicitudId = 20L;
        Mascota mascota = mascota(10L, EstadoMascota.DISPONIBLE);
        SolicitudAdopcion solicitud = solicitud(solicitudId, mascota, EstadoSolicitud.PENDIENTE);
        when(solicitudAdopcionRepository.findById(solicitudId)).thenReturn(Optional.of(solicitud));
        when(solicitudAdopcionRepository.save(solicitud)).thenReturn(solicitud);

        solicitudService.decidirSolicitud(solicitudId, EstadoSolicitud.RECHAZADA, "Rechazada");

        assertEquals(EstadoMascota.DISPONIBLE, mascota.getEstado());
        verify(mascotaRepository, never()).save(any(Mascota.class));
        verify(seguimientoService, never()).registrarSeguimiento(any(), any(), any());
        verify(notificacionService).notificarCambioEstado(solicitud);
    }

    @Test
    void decidirSolicitudDebeRechazarDecisionDiferenteDeAprobadaORechazada() {
        assertThrows(
                IllegalArgumentException.class,
                () -> solicitudService.decidirSolicitud(20L, EstadoSolicitud.PENDIENTE, "Pendiente"));
    }

    @Test
    void listarSolicitudesPorAdoptanteDebeRetornarSoloLasDelAdoptante() {
        Long adoptanteId = 1L;
        SolicitudAdopcion propia = solicitud(1L, mascota(10L, EstadoMascota.DISPONIBLE), EstadoSolicitud.PENDIENTE);
        when(solicitudAdopcionRepository.findByAdoptante_Id(adoptanteId))
                .thenReturn(List.of(propia));

        List<SolicitudAdopcion> resultado = solicitudService.listarSolicitudesPorAdoptante(adoptanteId);

        assertEquals(List.of(propia), resultado);
        verify(solicitudAdopcionRepository).findByAdoptante_Id(adoptanteId);
    }

    private Adoptante adoptante(Long id) {
        Adoptante adoptante = new Adoptante();
        adoptante.setId(id);
        adoptante.setCorreo("adoptante@example.com");
        return adoptante;
    }

    private Mascota mascota(Long id, EstadoMascota estado) {
        Mascota mascota = new Mascota();
        mascota.setId(id);
        mascota.setEstado(estado);
        return mascota;
    }

    private SolicitudAdopcion solicitud(Long id, Mascota mascota, EstadoSolicitud estado) {
        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setId(id);
        solicitud.setMascota(mascota);
        solicitud.setEstado(estado);
        return solicitud;
    }
}
