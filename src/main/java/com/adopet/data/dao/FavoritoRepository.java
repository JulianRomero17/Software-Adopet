package com.adopet.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adopet.business.models.Favorito;
import com.adopet.business.models.Mascota;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    @Query("select favorito from Favorito favorito join fetch favorito.mascota where favorito.adoptante.id = :adoptanteId")
    List<Favorito> findByAdoptanteIdWithMascota(@Param("adoptanteId") Long adoptanteId);

    boolean existsByAdoptante_IdAndMascota_Id(Long adoptanteId, Long mascotaId);

    void deleteByAdoptante_IdAndMascota_Id(Long adoptanteId, Long mascotaId);
}
