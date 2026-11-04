package com.adopet.business.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adopet.business.exceptions.MascotaNoEncontradaException;
import com.adopet.business.models.Adoptante;
import com.adopet.business.models.Favorito;
import com.adopet.business.models.Mascota;
import com.adopet.data.dao.FavoritoRepository;
import com.adopet.data.dao.MascotaRepository;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final MascotaRepository mascotaRepository;

    public FavoritoService(FavoritoRepository favoritoRepository, MascotaRepository mascotaRepository) {
        this.favoritoRepository = favoritoRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @Transactional(readOnly = true)
    public List<Mascota> listar(Long adoptanteId) {
        return favoritoRepository.findByAdoptanteIdWithMascota(adoptanteId).stream()
                .map(Favorito::getMascota)
                .toList();
    }

    @Transactional
    public void agregar(Adoptante adoptante, Long mascotaId) {
        if (favoritoRepository.existsByAdoptante_IdAndMascota_Id(adoptante.getId(), mascotaId)) {
            return;
        }

        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new MascotaNoEncontradaException(mascotaId));
        Favorito favorito = new Favorito();
        favorito.setAdoptante(adoptante);
        favorito.setMascota(mascota);
        favoritoRepository.save(favorito);
    }

    @Transactional
    public void quitar(Long adoptanteId, Long mascotaId) {
        favoritoRepository.deleteByAdoptante_IdAndMascota_Id(adoptanteId, mascotaId);
    }
}
