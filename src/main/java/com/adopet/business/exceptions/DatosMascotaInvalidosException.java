package com.adopet.business.exceptions;

public class DatosMascotaInvalidosException extends RuntimeException {

    public DatosMascotaInvalidosException(String mensaje) {
        super(mensaje);
    }
}