package com.adopet.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopet.business.exceptions.AccesoNoAutorizadoException;
import com.adopet.business.exceptions.SolicitudNoEncontradaException;
import com.adopet.business.models.Seguimiento;
import com.adopet.business.models.SolicitudAdopcion;
import com.adopet.business.models.Usuario;
import com.adopet.business.services.SeguimientoService;
import com.adopet.business.services.SolicitudAdopcionService;
import com.adopet.business.services.UsuarioService;
import com.adopet.presentation.dtos.MensajeResponse;
import com.adopet.presentation.dtos.SeguimientoResponseDTO;
import com.adopet.presentation.dtos.SolicitudAdopcionResponseDTO;

@RestController
public class SeguimientoController {

    private final SeguimientoService seguimientoService;
    private final SolicitudAdopcionService solicitudService;
    private final UsuarioService usuarioService;

    public SeguimientoController(
            SeguimientoService seguimientoService,
            SolicitudAdopcionService solicitudService,
            UsuarioService usuarioService) {
        this.seguimientoService = seguimientoService;
        this.solicitudService = solicitudService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/api/solicitudes/{id}/seguimiento/mio")
    @PreAuthorize("hasRole('ADOPTANTE')")
    public ResponseEntity<List<SeguimientoResponseDTO>> listarSeguimientoMio(
            @PathVariable Long id,
            Authentication authentication) {
        Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
        List<Seguimiento> seguimientos = seguimientoService.listarSeguimientoPorAdoptante(
                usuario.getId(), id);
        return ResponseEntity.ok(seguimientos.stream().map(SeguimientoResponseDTO::desde).toList());
    }

    @GetMapping("/api/historial")
    @PreAuthorize("hasAnyRole('ADOPTANTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<SolicitudAdopcionResponseDTO>> listarHistorial(
            Authentication authentication) {
        Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
        List<SolicitudAdopcion> historial = solicitudService
                .listarHistorial(usuario.getId(), usuario.getRol());
        return ResponseEntity.ok(historial.stream()
                .map(SolicitudAdopcionResponseDTO::desde).toList());
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<MensajeResponse> manejarAccesoNoAutorizado(
            AccesoNoAutorizadoException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(SolicitudNoEncontradaException.class)
    public ResponseEntity<MensajeResponse> manejarSolicitudNoEncontrada(
            SolicitudNoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeResponse(exception.getMessage()));
    }
}