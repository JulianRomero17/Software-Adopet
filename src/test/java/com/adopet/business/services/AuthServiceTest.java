package com.adopet.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.adopet.business.exceptions.CorreoYaRegistradoException;
import com.adopet.business.exceptions.CredencialesInvalidasException;
import com.adopet.business.exceptions.RestablecimientoInvalidoException;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.EstadoSolicitud;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.UsuarioRepository;
import com.adopet.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(usuarioRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void registrarDebeLanzarExcepcionSiElCorreoYaExiste() {
        Adoptante adoptante = adoptante("ana@example.com", "password");
        when(usuarioRepository.existsByCorreo(adoptante.getCorreo())).thenReturn(true);

        assertThrows(
                CorreoYaRegistradoException.class,
                () -> authService.registrar(adoptante));
    }

    @Test
    void registrarDebeGuardarLaContrasenaHasheada() {
        Adoptante adoptante = adoptante("ana@example.com", "password");
        when(usuarioRepository.existsByCorreo(adoptante.getCorreo())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("$2a$10$hash-de-ejemplo");
        when(usuarioRepository.save(adoptante)).thenReturn(adoptante);

        authService.registrar(adoptante);

        ArgumentCaptor<Adoptante> captor = ArgumentCaptor.forClass(Adoptante.class);
        verify(usuarioRepository).save(captor.capture());
        assertNotEquals("password", captor.getValue().getContrasena());
        assertEquals("$2a$10$hash-de-ejemplo", captor.getValue().getContrasena());
    }

    @Test
    void loginDebeRetornarTokenYRolConCredencialesValidas() {
        Usuario usuario = usuario(1L, "ana@example.com", "$2a$10$hash", RolUsuario.ADOPTANTE, true);
        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password", usuario.getContrasena())).thenReturn(true);
        when(jwtUtil.generarToken(usuario)).thenReturn("jwt-token");

        AuthService.LoginResult resultado = authService.login(usuario.getCorreo(), "password");

        assertEquals("jwt-token", resultado.token());
        assertEquals(RolUsuario.ADOPTANTE, resultado.rol());
    }

    @Test
    void loginDebeFallarConContrasenaIncorrecta() {
        Usuario usuario = usuario(1L, "ana@example.com", "$2a$10$hash", RolUsuario.ADOPTANTE, true);
        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("incorrecta", usuario.getContrasena())).thenReturn(false);

        assertThrows(
                CredencialesInvalidasException.class,
                () -> authService.login(usuario.getCorreo(), "incorrecta"));
    }

    @Test
    void loginDebeFallarSiElCorreoNoExiste() {
        when(usuarioRepository.findByCorreo("inexistente@example.com")).thenReturn(Optional.empty());

        assertThrows(
                CredencialesInvalidasException.class,
                () -> authService.login("inexistente@example.com", "password"));
    }

    @Test
    void generarTokenRecuperacionDebeAsociarElTokenAlUsuarioCorrecto() {
        Usuario usuario = usuario(1L, "ana@example.com", "$2a$10$hash", RolUsuario.ADOPTANTE, true);
        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));

        String token = authService.generarTokenRecuperacion(usuario.getCorreo());

        assertNotEquals(null, token);
        verify(usuarioRepository).findByCorreo("ana@example.com");
    }

    @Test
    void restablecerContrasenaDebeFallarSiElTokenNoExiste() {
        assertThrows(
                RestablecimientoInvalidoException.class,
                () -> authService.restablecerContrasena("token-inexistente", "nueva-password"));
    }

    @Test
    void restablecerContrasenaDebeActualizarLaContrasenaHasheada() {
        Usuario usuario = usuario(1L, "ana@example.com", "$2a$10$hash-anterior", RolUsuario.ADOPTANTE, true);
        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nueva-password")).thenReturn("$2a$10$hash-nuevo");

        String token = authService.generarTokenRecuperacion(usuario.getCorreo());
        authService.restablecerContrasena(token, "nueva-password");

        verify(passwordEncoder).encode("nueva-password");
        verify(usuarioRepository).save(usuario);
        assertNotEquals("nueva-password", usuario.getContrasena());
        assertEquals("$2a$10$hash-nuevo", usuario.getContrasena());
    }

    private Adoptante adoptante(String correo, String contrasena) {
        Adoptante adoptante = new Adoptante();
        adoptante.setCorreo(correo);
        adoptante.setContrasena(contrasena);
        return adoptante;
    }

    private Usuario usuario(
            Long id,
            String correo,
            String contrasena,
            RolUsuario rol,
            boolean activo) {
        Adoptante usuario = new Adoptante();
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setRol(rol);
        usuario.setActivo(activo);
        return usuario;
    }
}
