package com.adopet.business.exceptions;

public class MascotaNoDisponibleException extends RuntimeException {

    public MascotaNoDisponibleException(Long id) {
        super("La mascota con id " + id + " no está disponible para adopción");
    }
}