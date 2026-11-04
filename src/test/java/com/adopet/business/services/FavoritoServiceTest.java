package com.adopet.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adopet.business.models.Adoptante;
import com.adopet.business.models.Favorito;
import com.adopet.business.models.Mascota;
import com.adopet.data.dao.FavoritoRepository;
import com.adopet.data.dao.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    private FavoritoService favoritoService;

    @BeforeEach
    void setUp() {
        favoritoService = new FavoritoService(favoritoRepository, mascotaRepository);
    }

    @Test
    void listarRetornaLasMascotasFavoritasDelAdoptante() {
        Long adoptanteId = 7L;
        Mascota mascota = new Mascota();
        Favorito favorito = new Favorito();
        favorito.setMascota(mascota);
        when(favoritoRepository.findByAdoptanteIdWithMascota(adoptanteId))
                .thenReturn(List.of(favorito));

        assertEquals(List.of(mascota), favoritoService.listar(adoptanteId));
    }

    @Test
    void agregarGuardaElFavoritoSiTodaviaNoExiste() {
        Adoptante adoptante = new Adoptante();
        adoptante.setId(7L);
        Mascota mascota = new Mascota();
        mascota.setId(12L);
        when(favoritoRepository.existsByAdoptante_IdAndMascota_Id(7L, 12L)).thenReturn(false);
        when(mascotaRepository.findById(12L)).thenReturn(Optional.of(mascota));

        favoritoService.agregar(adoptante, 12L);

        ArgumentCaptor<Favorito> captor = ArgumentCaptor.forClass(Favorito.class);
        verify(favoritoRepository).save(captor.capture());
        assertEquals(adoptante, captor.getValue().getAdoptante());
        assertEquals(mascota, captor.getValue().getMascota());
    }

    @Test
    void agregarNoDuplicaUnFavoritoExistente() {
        Adoptante adoptante = new Adoptante();
        adoptante.setId(7L);
        when(favoritoRepository.existsByAdoptante_IdAndMascota_Id(7L, 12L)).thenReturn(true);

        favoritoService.agregar(adoptante, 12L);

        verify(favoritoRepository, never()).save(any(Favorito.class));
        verify(mascotaRepository, never()).findById(12L);
    }

    @Test
    void quitarEliminaLaRelacionDeEseAdoptanteConLaMascota() {
        favoritoService.quitar(7L, 12L);

        verify(favoritoRepository).deleteByAdoptante_IdAndMascota_Id(7L, 12L);
    }
}
