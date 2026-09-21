package com.adopet.business.exceptions;

public class SolicitudNoEncontradaException extends RuntimeException {

    public SolicitudNoEncontradaException(Long id) {
        super("No se encontró la solicitud de adopción con id: " + id);
    }
}