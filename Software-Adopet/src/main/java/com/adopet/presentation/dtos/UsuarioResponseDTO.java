package com.adopet.presentation.dtos;

import com.adopet.business.models.RolUsuario;
import com.adopet.business.models.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String documento,
        String correo,
        String telefono,
        String direccion,
        RolUsuario rol,
        boolean activo) {

    public static UsuarioResponseDTO desde(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getDocumento(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.isActivo());
    }
}