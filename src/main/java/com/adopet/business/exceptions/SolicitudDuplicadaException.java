package com.adopet.business.exceptions;

public class SolicitudDuplicadaException extends RuntimeException {

    public SolicitudDuplicadaException(Long adoptanteId, Long mascotaId) {
        super("Ya existe una solicitud pendiente para el adoptante "
                + adoptanteId + " y la mascota " + mascotaId);
    }
}