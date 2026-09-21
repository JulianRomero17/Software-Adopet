package com.adopet.business.exceptions;

public class SolicitudAccesoDenegadoException extends RuntimeException {

    public SolicitudAccesoDenegadoException(Long id) {
        super("No tiene permiso para consultar la solicitud con id: " + id);
    }
}