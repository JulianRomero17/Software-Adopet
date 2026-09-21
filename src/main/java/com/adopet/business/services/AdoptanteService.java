package com.adopet.business.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adopet.business.models.Adoptante;
import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;
import com.adopet.business.exceptions.AdoptanteNoEncontradoException;
import com.adopet.business.exceptions.CorreoYaRegistradoException;
import com.adopet.data.dao.UsuarioRepository;

@Service
public class AdoptanteService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdoptanteService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Adoptante registrar(Adoptante adoptante) {
        if (usuarioRepository.existsByCorreo(adoptante.getCorreo())) {
            throw new CorreoYaRegistradoException(adoptante.getCorreo());
        }

        adoptante.setRol(RolUsuario.ADOPTANTE);
        adoptante.setActivo(true);
        adoptante.setContrasena(passwordEncoder.encode(adoptante.getContrasena()));

        usuarioRepository.save(adoptante);
        return adoptante;
    }

    public Adoptante obtenerPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new AdoptanteNoEncontradoException(null));
        if (!(usuario instanceof Adoptante adoptante)) {
            throw new AdoptanteNoEncontradoException(usuario.getId());
        }
        return adoptante;
    }
}