package com.adopet.business.exceptions;

public class MascotaNoEncontradaException extends RuntimeException {

    public MascotaNoEncontradaException(Long id) {
        super("No se encontró la mascota con id: " + id);
    }
}