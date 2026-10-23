package com.adopet.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopet.business.exceptions.AdoptanteNoEncontradoException;
import com.adopet.business.exceptions.MascotaNoDisponibleException;
import com.adopet.business.exceptions.SolicitudAccesoDenegadoException;
import com.adopet.business.exceptions.SolicitudDuplicadaException;
import com.adopet.business.exceptions.SolicitudNoEncontradaException;
import com.adopet.business.models.SolicitudAdopcion;
import com.adopet.business.models.Seguimiento;
import com.adopet.business.services.AdoptanteService;
import com.adopet.business.services.SeguimientoService;
import com.adopet.business.services.SolicitudAdopcionService;
import com.adopet.presentation.dtos.CrearSolicitudRequestDTO;
import com.adopet.presentation.dtos.DecidirSolicitudRequestDTO;
import com.adopet.presentation.dtos.MensajeResponse;
import com.adopet.presentation.dtos.SeguimientoResponseDTO;
import com.adopet.presentation.dtos.SolicitudAdopcionResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudAdopcionController {

    private final SolicitudAdopcionService solicitudService;
    private final SeguimientoService seguimientoService;
    private final AdoptanteService adoptanteService;

    public SolicitudAdopcionController(
            SolicitudAdopcionService solicitudService,
            SeguimientoService seguimientoService,
            AdoptanteService adoptanteService) {
        this.solicitudService = solicitudService;
        this.seguimientoService = seguimientoService;
        this.adoptanteService = adoptanteService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADOPTANTE')")
    public ResponseEntity<SolicitudAdopcionResponseDTO> crear(
            @Valid @RequestBody CrearSolicitudRequestDTO request,
            Authentication authentication) {
        Long adoptanteId = adoptanteService.obtenerPorCorreo(authentication.getName()).getId();
        SolicitudAdopcion solicitud = solicitudService.crearSolicitud(adoptanteId, request.mascotaId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SolicitudAdopcionResponseDTO.desde(solicitud));
    }

    @GetMapping("/mias")
    @PreAuthorize("hasRole('ADOPTANTE')")
    public ResponseEntity<List<SolicitudAdopcionResponseDTO>> listarMias(Authentication authentication) {
        Long adoptanteId = adoptanteService.obtenerPorCorreo(authentication.getName()).getId();
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorAdoptante(adoptanteId)
                .stream().map(SolicitudAdopcionResponseDTO::desde).toList());
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<SolicitudAdopcionResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(solicitudService.listarSolicitudesPendientes()
                .stream().map(SolicitudAdopcionResponseDTO::desde).toList());
    }

    @PatchMapping("/{id}/decision")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<SolicitudAdopcionResponseDTO> decidir(
            @PathVariable Long id,
            @Valid @RequestBody DecidirSolicitudRequestDTO request) {
        SolicitudAdopcion solicitud = solicitudService.decidirSolicitud(
                id, request.decision(), request.observaciones());
        return ResponseEntity.ok(SolicitudAdopcionResponseDTO.desde(solicitud));
    }

    @GetMapping("/{id}/seguimiento")
    @PreAuthorize("hasAnyRole('ADOPTANTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<SeguimientoResponseDTO>> listarSeguimiento(
            @PathVariable Long id,
            Authentication authentication) {
        boolean administrador = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMINISTRADOR"));
        Long adoptanteId = administrador
                ? null
                : adoptanteService.obtenerPorCorreo(authentication.getName()).getId();
        List<Seguimiento> seguimientos = seguimientoService.listarSeguimientoAutorizado(
                id, adoptanteId, administrador);
        return ResponseEntity.ok(seguimientos.stream().map(SeguimientoResponseDTO::desde).toList());
    }

        @ExceptionHandler({ MascotaNoDisponibleException.class, SolicitudDuplicadaException.class })
    public ResponseEntity<MensajeResponse> manejarConflicto(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(SolicitudAccesoDenegadoException.class)
    public ResponseEntity<MensajeResponse> manejarAccesoDenegado(
            SolicitudAccesoDenegadoException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(SolicitudNoEncontradaException.class)
    public ResponseEntity<MensajeResponse> manejarSolicitudNoEncontrada(
            SolicitudNoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(AdoptanteNoEncontradoException.class)
    public ResponseEntity<MensajeResponse> manejarAdoptanteNoEncontrado(
            AdoptanteNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensajeResponse> manejarSolicitudInvalida(
            IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new MensajeResponse(exception.getMessage()));
    }
}