package com.adopet.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adopet.business.models.EstadoSolicitud;
import com.adopet.business.models.SolicitudAdopcion;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {

	boolean existsByAdoptante_IdAndMascota_IdAndEstado(
			Long adoptanteId, Long mascotaId, EstadoSolicitud estado);

	List<SolicitudAdopcion> findByAdoptante_Id(Long adoptanteId);

	List<SolicitudAdopcion> findByAdoptante_IdAndEstadoIn(
			Long adoptanteId, List<EstadoSolicitud> estados);

	List<SolicitudAdopcion> findByEstadoIn(List<EstadoSolicitud> estados);
}