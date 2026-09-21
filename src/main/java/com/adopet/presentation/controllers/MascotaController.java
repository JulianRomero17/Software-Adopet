package com.adopet.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adopet.business.exceptions.DatosMascotaInvalidosException;
import com.adopet.business.exceptions.MascotaNoEncontradaException;
import com.adopet.business.models.Mascota;
import com.adopet.business.services.MascotaService;
import com.adopet.presentation.dtos.MascotaRequestDTO;
import com.adopet.presentation.dtos.MascotaResponseDTO;
import com.adopet.presentation.dtos.MensajeResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MascotaResponseDTO> registrar(@Valid @RequestBody MascotaRequestDTO request) {
        Mascota mascota = mascotaService.registrarMascota(convertir(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(MascotaResponseDTO.desde(mascota));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MascotaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO request) {
        Mascota mascota = mascotaService.actualizarMascota(id, convertir(request));
        return ResponseEntity.ok(MascotaResponseDTO.desde(mascota));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MascotaResponseDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(MascotaResponseDTO.desde(mascotaService.desactivarMascota(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADOPTANTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<MascotaResponseDTO>> listar(
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) String raza,
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) String tamano) {
        List<Mascota> mascotas = parametrosVacios(especie, raza, sexo, edadMin, edadMax, tamano)
                ? mascotaService.listarDisponibles()
                : mascotaService.buscarYFiltrar(especie, raza, sexo, edadMin, edadMax, tamano);
        return ResponseEntity.ok(mascotas.stream().map(MascotaResponseDTO::desde).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADOPTANTE', 'ADMINISTRADOR')")
        public ResponseEntity<MascotaResponseDTO> obtenerDetalle(
            @PathVariable Long id, Authentication authentication) {
        boolean administrador = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMINISTRADOR"));
        Mascota mascota = administrador
            ? mascotaService.obtenerDetalleAdministrador(id)
            : mascotaService.obtenerDetalle(id);
        return ResponseEntity.ok(MascotaResponseDTO.desde(mascota));
    }

    @ExceptionHandler(MascotaNoEncontradaException.class)
    public ResponseEntity<MensajeResponse> manejarMascotaNoEncontrada(
            MascotaNoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(DatosMascotaInvalidosException.class)
    public ResponseEntity<MensajeResponse> manejarDatosInvalidos(
            DatosMascotaInvalidosException exception) {
        return ResponseEntity.badRequest().body(new MensajeResponse(exception.getMessage()));
    }

    private Mascota convertir(MascotaRequestDTO request) {
        Mascota mascota = new Mascota();
        mascota.setNombre(request.nombre());
        mascota.setEspecie(request.especie());
        mascota.setRaza(request.raza());
        mascota.setSexo(request.sexo());
        mascota.setEdad(request.edad());
        mascota.setTamano(request.tamano());
        mascota.setDescripcion(request.descripcion());
        mascota.setEstadoSalud(request.estadoSalud());
        mascota.setImagenUrl(request.imagenUrl());
        return mascota;
    }

    private boolean parametrosVacios(
            String especie,
            String raza,
            String sexo,
            Integer edadMin,
            Integer edadMax,
            String tamano) {
        return especie == null && raza == null && sexo == null
                && edadMin == null && edadMax == null && tamano == null;
    }
}