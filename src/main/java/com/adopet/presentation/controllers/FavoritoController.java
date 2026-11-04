package com.adopet.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopet.business.exceptions.MascotaNoEncontradaException;
import com.adopet.business.services.AdoptanteService;
import com.adopet.business.services.FavoritoService;
import com.adopet.presentation.dtos.MensajeResponse;
import com.adopet.presentation.dtos.MascotaResponseDTO;

@RestController
@RequestMapping("/api/favoritos")
@PreAuthorize("hasRole('ADOPTANTE')")
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final AdoptanteService adoptanteService;

    public FavoritoController(FavoritoService favoritoService, AdoptanteService adoptanteService) {
        this.favoritoService = favoritoService;
        this.adoptanteService = adoptanteService;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> listar(Authentication authentication) {
        Long adoptanteId = adoptanteService.obtenerPorCorreo(authentication.getName()).getId();
        return ResponseEntity.ok(favoritoService.listar(adoptanteId).stream()
                .map(MascotaResponseDTO::desde)
                .toList());
    }

    @PutMapping("/{mascotaId}")
    public ResponseEntity<Void> agregar(
            @PathVariable Long mascotaId,
            Authentication authentication) {
        favoritoService.agregar(
                adoptanteService.obtenerPorCorreo(authentication.getName()),
                mascotaId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{mascotaId}")
    public ResponseEntity<Void> quitar(
            @PathVariable Long mascotaId,
            Authentication authentication) {
        Long adoptanteId = adoptanteService.obtenerPorCorreo(authentication.getName()).getId();
        favoritoService.quitar(adoptanteId, mascotaId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MascotaNoEncontradaException.class)
    public ResponseEntity<MensajeResponse> manejarMascotaNoEncontrada(
            MascotaNoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeResponse(exception.getMessage()));
    }
}
