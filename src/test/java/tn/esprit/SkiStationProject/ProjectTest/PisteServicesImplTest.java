package tn.esprit.SkiStationProject.ProjectTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.SkiStationProject.entities.Piste;
import tn.esprit.SkiStationProject.repositories.PisteRepository;
import tn.esprit.SkiStationProject.services.PisteServicesImpl;

public class PisteServicesImplTest {

    @Mock
    private PisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRetrieveAllPistes() {
        // Given
        List<Piste> pistes = new ArrayList<>();
        pistes.add(new Piste());
        pistes.add(new Piste());
        when(pisteRepository.findAll()).thenReturn(pistes);

        // When
        List<Piste> result = pisteService.retrieveAllPistes();

        // Then
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        // Given
        Piste pisteToAdd = new Piste();
        when(pisteRepository.save(pisteToAdd)).thenReturn(pisteToAdd);

        // When
        Piste result = pisteService.addPiste(pisteToAdd);

        // Then
        assertEquals(pisteToAdd, result);
        verify(pisteRepository, times(1)).save(pisteToAdd);
    }

    @Test
    void testRemovePiste() {
        // Given
        Long pisteIdToRemove = 1L;

        // When
        pisteService.removePiste(pisteIdToRemove);

        // Then
        verify(pisteRepository, times(1)).deleteById(pisteIdToRemove);
    }

    @Test
    void testRetrievePiste() {
        // Given
        Long pisteId = 1L;
        Piste piste = new Piste();
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));

        // When
        Piste result = pisteService.retrievePiste(pisteId);

        // Then
        assertEquals(piste, result);
        verify(pisteRepository, times(1)).findById(pisteId);
    }

    @Test
    void testRetrievePiste_NotFound() {
        // Given
        Long pisteId = 1L;
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pisteService.retrievePiste(pisteId);
        });
        assertEquals("no piste found with this id " + pisteId, exception.getMessage());
        verify(pisteRepository, times(1)).findById(pisteId);
    }
}