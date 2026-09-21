package com.adopet.business.exceptions;

public class AdoptanteNoEncontradoException extends RuntimeException {

    public AdoptanteNoEncontradoException(Long id) {
        super("No se encontró el adoptante con id: " + id);
    }
}