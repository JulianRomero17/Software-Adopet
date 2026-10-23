package com.adopet.business.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adopet.business.exceptions.CredencialesInvalidasException;
import com.adopet.business.exceptions.CorreoYaRegistradoException;
import com.adopet.business.exceptions.RestablecimientoInvalidoException;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.UsuarioRepository;
import com.adopet.security.JwtUtil;

@Service
public class AuthService {

    private static final Duration DURACION_TOKEN = Duration.ofMinutes(15);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Map<String, TokenRecuperacion> tokensRecuperacion;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokensRecuperacion = new ConcurrentHashMap<>();
    }

    public LoginResult login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!usuario.isActivo() || !passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new CredencialesInvalidasException();
        }

        return new LoginResult(jwtUtil.generarToken(usuario), usuario.getRol());
    }

    public Adoptante registrar(Adoptante adoptante) {
        if (usuarioRepository.existsByCorreo(adoptante.getCorreo())) {
            throw new CorreoYaRegistradoException(adoptante.getCorreo());
        }

        adoptante.setRol(RolUsuario.ADOPTANTE);
        adoptante.setActivo(true);
        adoptante.setContrasena(passwordEncoder.encode(adoptante.getContrasena()));
        return usuarioRepository.save(adoptante);
    }

    public String generarTokenRecuperacion(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ese correo"));

        String token = UUID.randomUUID().toString();
        tokensRecuperacion.put(token, new TokenRecuperacion(usuario.getCorreo(), Instant.now()));
        enviarCorreoRecuperacion(usuario.getCorreo(), token);
        return token;
    }

    public void validarTokenYActualizarContrasena(String token, String nuevaContrasena) {
        TokenRecuperacion tokenRecuperacion = tokensRecuperacion.get(token);
        if (tokenRecuperacion == null || tokenRecuperacion.expirado()) {
            tokensRecuperacion.remove(token);
            throw new RestablecimientoInvalidoException("El token de recuperación no es válido o expiró");
        }

        Usuario usuario = usuarioRepository.findByCorreo(tokenRecuperacion.correo())
                .orElseThrow(() -> new RestablecimientoInvalidoException("El usuario ya no existe"));
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
        tokensRecuperacion.remove(token);
    }

    public void restablecerContrasena(String token, String nuevaContrasena) {
        validarTokenYActualizarContrasena(token, nuevaContrasena);
    }

    public void enviarCorreoRecuperacion(String correo, String token) {
        // TODO: integrar el envío real de correo.
    }

    private record TokenRecuperacion(String correo, Instant creadoEn) {

        private boolean expirado() {
            return creadoEn.plus(DURACION_TOKEN).isBefore(Instant.now());
        }
    }

    public record LoginResult(String token, RolUsuario rol) {
    }
}