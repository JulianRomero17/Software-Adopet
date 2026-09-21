package com.adopet.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adopet.business.exceptions.UsuarioNoEncontradoException;
import com.adopet.business.models.Administrador;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void listarAdoptantesDebeRetornarSoloUsuariosAdoptante() {
        Adoptante adoptante = usuario(1L, "Ana", "ana@example.com", "hash-adoptante", true);
        Administrador administrador = new Administrador();
        administrador.setId(2L);
        administrador.setNombre("Admin");
        administrador.setCorreo("admin@example.com");
        administrador.setRol(RolUsuario.ADMINISTRADOR);

        when(usuarioRepository.findByRol(RolUsuario.ADOPTANTE))
                .thenReturn(List.of(adoptante, administrador));

        List<Usuario> resultado = usuarioService.listarAdoptantes();

        assertEquals(List.of(adoptante), resultado);
        verify(usuarioRepository).findByRol(RolUsuario.ADOPTANTE);
    }

    @Test
    void consultarUsuarioDebeLanzarExcepcionSiNoExiste() {
        Long usuarioId = 99L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioService.consultarUsuario(usuarioId));
    }

    @Test
    void activarDesactivarUsuarioDebeActualizarEstado() {
        Adoptante usuario = usuario(1L, "Ana", "ana@example.com", "hash-original", true);
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.activarDesactivarUsuario(usuario.getId(), false);

        assertEquals(false, resultado.isActivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void activarDesactivarUsuarioNoDebeModificarOtrosCampos() {
        Adoptante usuario = usuario(1L, "Ana", "ana@example.com", "hash-original", true);
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        usuarioService.activarDesactivarUsuario(usuario.getId(), false);

        assertEquals("Ana", usuario.getNombre());
        assertEquals("ana@example.com", usuario.getCorreo());
        assertEquals("hash-original", usuario.getContrasena());
        assertEquals(false, usuario.isActivo());
    }

    private Adoptante usuario(
            Long id,
            String nombre,
            String correo,
            String contrasena,
            boolean activo) {
        Adoptante usuario = new Adoptante();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setRol(RolUsuario.ADOPTANTE);
        usuario.setActivo(activo);
        return usuario;
    }
}