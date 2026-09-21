package com.adopet.business.exceptions;

public class RestablecimientoInvalidoException extends RuntimeException {

    public RestablecimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}