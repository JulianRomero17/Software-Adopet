package com.adopet.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adopet.business.exceptions.CorreoYaRegistradoException;
import com.adopet.business.models.Adoptante;
import com.adopet.business.services.AdoptanteService;
import com.adopet.business.services.AuthService;
import com.adopet.presentation.dtos.LoginRequest;
import com.adopet.presentation.dtos.LoginResponse;
import com.adopet.presentation.dtos.MensajeResponse;
import com.adopet.presentation.dtos.RecuperarContrasenaRequest;
import com.adopet.presentation.dtos.RecuperarContrasenaResponse;
import com.adopet.presentation.dtos.RegistroRequest;
import com.adopet.presentation.dtos.RestablecerContrasenaRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdoptanteService adoptanteService;
    private final AuthService authService;

    public AuthController(AdoptanteService adoptanteService, AuthService authService) {
        this.adoptanteService = adoptanteService;
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<MensajeResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        Adoptante adoptante = new Adoptante();
        adoptante.setNombre(request.nombre());
        adoptante.setDocumento(request.documento());
        adoptante.setCorreo(request.correo());
        adoptante.setTelefono(request.telefono());
        adoptante.setDireccion(request.direccion());
        adoptante.setContrasena(request.contrasena());

        adoptanteService.registrar(adoptante);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MensajeResponse("Cuenta creada correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthService.LoginResult resultado = authService.login(request.correo(), request.contrasena());
        return ResponseEntity.ok(new LoginResponse(resultado.token(), resultado.rol()));
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<RecuperarContrasenaResponse> recuperarContrasena(
            @Valid @RequestBody RecuperarContrasenaRequest request) {
        String token = authService.generarTokenRecuperacion(request.correo());
        return ResponseEntity.ok(new RecuperarContrasenaResponse(
                "Token de recuperación generado", token));
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<MensajeResponse> restablecerContrasena(
            @Valid @RequestBody RestablecerContrasenaRequest request) {
        authService.validarTokenYActualizarContrasena(request.token(), request.nuevaContrasena());
        return ResponseEntity.ok(new MensajeResponse("Contraseña actualizada correctamente"));
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<MensajeResponse> manejarCorreoYaRegistrado(
            CorreoYaRegistradoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensajeResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensajeResponse> manejarSolicitudInvalida(
            IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new MensajeResponse(exception.getMessage()));
    }
}