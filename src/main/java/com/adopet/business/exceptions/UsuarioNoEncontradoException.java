package com.adopet.business.exceptions;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("No se encontró el usuario con id: " + id);
    }

    public UsuarioNoEncontradoException(String correo) {
        super("No se encontró el usuario con correo: " + correo);
    }
}