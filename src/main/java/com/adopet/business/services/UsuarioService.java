package com.adopet.business.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.adopet.business.exceptions.UsuarioNoEncontradoException;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.data.dao.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarAdoptantes() {
        return usuarioRepository.findByRol(RolUsuario.ADOPTANTE).stream()
                .filter(usuario -> usuario.getRol() == RolUsuario.ADOPTANTE)
                .toList();
    }

    public Usuario consultarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    public Usuario activarDesactivarUsuario(Long id, boolean estado) {
        Usuario usuario = consultarUsuario(id);
        usuario.setActivo(estado);
        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException(correo));
    }
}