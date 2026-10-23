package com.adopet.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adopet.business.models.Seguimiento;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

	List<Seguimiento> findBySolicitudAdopcion_Id(Long solicitudId);
}