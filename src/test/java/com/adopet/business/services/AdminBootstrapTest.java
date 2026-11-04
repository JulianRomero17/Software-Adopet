package com.adopet.business.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.adopet.business.models.Administrador;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.RolUsuario;
import com.adopet.data.dao.UsuarioRepository;

class AdminBootstrapTest {

    private final UsuarioRepository usuarioRepository = org.mockito.Mockito.mock(UsuarioRepository.class);
    private final PasswordEncoder passwordEncoder = org.mockito.Mockito.mock(PasswordEncoder.class);

    @Test
    void creaAdministradorConContrasenaCodificadaCuandoNoExiste() {
        when(usuarioRepository.findByCorreo("admin@adopet.test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("una-clave-segura-123")).thenReturn("hash");

        AdminBootstrap bootstrap = bootstrap(true, false, "admin@adopet.test", "una-clave-segura-123");
        bootstrap.run(new DefaultApplicationArguments(new String[0]));

        verify(usuarioRepository).save(any(Administrador.class));
        verify(passwordEncoder).encode("una-clave-segura-123");
    }

    @Test
    void noPromueveUnaCuentaExistenteDeAdoptante() {
        Adoptante adoptante = new Adoptante();
        adoptante.setRol(RolUsuario.ADOPTANTE);
        when(usuarioRepository.findByCorreo("admin@adopet.test")).thenReturn(Optional.of(adoptante));

        AdminBootstrap bootstrap = bootstrap(true, false, "admin@adopet.test", "una-clave-segura-123");

        assertThrows(IllegalStateException.class,
                () -> bootstrap.run(new DefaultApplicationArguments(new String[0])));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void restableceContrasenaSoloCuandoSeSolicitaExplicitamente() {
        Administrador administrador = new Administrador();
        administrador.setRol(RolUsuario.ADMINISTRADOR);
        when(usuarioRepository.findByCorreo("admin@adopet.test")).thenReturn(Optional.of(administrador));
        when(passwordEncoder.encode("una-clave-segura-123")).thenReturn("hash-nuevo");

        AdminBootstrap bootstrap = bootstrap(true, true, "admin@adopet.test", "una-clave-segura-123");
        bootstrap.run(new DefaultApplicationArguments(new String[0]));

        verify(passwordEncoder).encode("una-clave-segura-123");
        verify(usuarioRepository).save(administrador);
        org.junit.jupiter.api.Assertions.assertEquals("hash-nuevo", administrador.getContrasena());
    }

    @Test
    void noHaceNadaCuandoElBootstrapEstaDeshabilitado() {
        AdminBootstrap bootstrap = bootstrap(false, false, "", "");

        bootstrap.run(new DefaultApplicationArguments(new String[0]));

        verify(usuarioRepository, never()).findByCorreo(org.mockito.ArgumentMatchers.anyString());
        verify(usuarioRepository, never()).save(any());
    }

    private AdminBootstrap bootstrap(boolean enabled, boolean resetPassword, String email, String password) {
        return new AdminBootstrap(usuarioRepository, passwordEncoder, enabled, resetPassword,
                "Administradora Adopet", "ADMIN-001", email, password);
    }
}
