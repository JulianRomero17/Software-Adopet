package com.adopet.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.adopet.business.models.EstadoMascota;
import com.adopet.business.models.Mascota;
import com.adopet.business.exceptions.DatosMascotaInvalidosException;
import com.adopet.data.dao.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    private MascotaService mascotaService;

    @BeforeEach
    void setUp() {
        mascotaService = new MascotaService(mascotaRepository);
    }

    @Test
    void listarTodasDebeIncluirMascotasSinFiltrarPorEstado() {
        List<Mascota> mascotas = List.of(new Mascota(), new Mascota());
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        assertEquals(mascotas, mascotaService.listarTodas());
        verify(mascotaRepository).findAll();
    }

    @Test
    void desactivarDebeGuardarMotivoYEstado() {
        Mascota mascota = new Mascota();
        mascota.setId(7L);
        mascota.setEstado(EstadoMascota.DISPONIBLE);
        when(mascotaRepository.findById(7L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(mascota)).thenReturn(mascota);

        Mascota resultado = mascotaService.desactivarMascota(7L, "  Pausa temporal  ");

        assertSame(mascota, resultado);
        assertEquals(EstadoMascota.NO_DISPONIBLE, resultado.getEstado());
        assertEquals("Pausa temporal", resultado.getMotivoDesactivacion());
        verify(mascotaRepository).save(mascota);
    }

    @Test
    void reactivarDebeDejarMascotaDisponibleYConservarMotivo() {
        Mascota mascota = new Mascota();
        mascota.setId(7L);
        mascota.setEstado(EstadoMascota.NO_DISPONIBLE);
        mascota.setMotivoDesactivacion("Pausa temporal");
        when(mascotaRepository.findById(7L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(mascota)).thenReturn(mascota);

        Mascota resultado = mascotaService.reactivarMascota(7L);

        assertEquals(EstadoMascota.DISPONIBLE, resultado.getEstado());
        assertEquals("Pausa temporal", resultado.getMotivoDesactivacion());
        verify(mascotaRepository).save(mascota);
    }

    @Test
    void desactivarDebeRechazarMotivoVacio() {
        org.junit.jupiter.api.Assertions.assertThrows(
                DatosMascotaInvalidosException.class,
                () -> mascotaService.desactivarMascota(7L, "  "));
    }

    @Test
    @SuppressWarnings("unchecked")
    void buscarPorRazaDebeCoincidirParcialmenteSinImportarMayusculasNiEspacios() {
        Root<Mascota> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        Path<EstadoMascota> estadoPath = mock(Path.class);
        Path<String> razaPath = mock(Path.class);
        Expression<String> razaEnMinusculas = mock(Expression.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<EstadoMascota>get("estado")).thenReturn(estadoPath);
        when(root.<String>get("raza")).thenReturn(razaPath);
        when(criteriaBuilder.equal(estadoPath, EstadoMascota.DISPONIBLE)).thenReturn(predicate);
        when(criteriaBuilder.lower(razaPath)).thenReturn(razaEnMinusculas);
        when(criteriaBuilder.like(razaEnMinusculas, "%pitbull%", '\\')).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(predicate);
        when(mascotaRepository.findAll(any(Specification.class))).thenAnswer(invocation -> {
            Specification<Mascota> specification = invocation.getArgument(0);
            specification.toPredicate(root, query, criteriaBuilder);
            return List.of();
        });

        mascotaService.buscarYFiltrar(null, "  PitBull  ", null, null, null, null);

        verify(criteriaBuilder).like(razaEnMinusculas, "%pitbull%", '\\');
    }
}
