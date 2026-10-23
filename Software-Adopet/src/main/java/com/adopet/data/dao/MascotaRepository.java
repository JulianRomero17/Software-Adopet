package com.adopet.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Long>, JpaSpecificationExecutor<Mascota> {

    List<Mascota> findByEstado(EstadoMascota estado);
}