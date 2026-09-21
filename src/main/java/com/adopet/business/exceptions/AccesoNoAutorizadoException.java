package com.adopet.business.exceptions;

public class AccesoNoAutorizadoException extends RuntimeException {

    public AccesoNoAutorizadoException(Long solicitudId) {
        super("El adoptante no tiene acceso a la solicitud con id: " + solicitudId);
    }
}