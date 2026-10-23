package com.adopet.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopet.business.exceptions.UsuarioNoEncontradoException;
import com.adopet.business.services.UsuarioService;
import com.adopet.presentation.dtos.EstadoUsuarioRequestDTO;
import com.adopet.presentation.dtos.MensajeResponse;
import com.adopet.presentation.dtos.UsuarioResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarAdoptantes() {
        return ResponseEntity.ok(usuarioService.listarAdoptantes().stream()
                .map(UsuarioResponseDTO::desde).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponseDTO.desde(usuarioService.consultarUsuario(id)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoUsuarioRequestDTO request) {
        return ResponseEntity.ok(UsuarioResponseDTO.desde(
                usuarioService.activarDesactivarUsuario(id, request.estado())));
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<MensajeResponse> manejarUsuarioNoEncontrado(
            UsuarioNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeResponse(exception.getMessage()));
    }
}