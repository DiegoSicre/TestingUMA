package org.mps.selection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;

public class TournamentSelectionTest {

    @Test
    @DisplayName("Lanza excepción si el tamaño del torneo es 0")
    public void constructorThrowsWhenTournamentSizeIsZero() {
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            new TournamentSelection(0);
        });
    }

    @Test
    @DisplayName("Lanza excepción si la población es null")
    public void selectThrowsWhenPopulationIsNull() throws EvolutionaryAlgorithmException {
        TournamentSelection ts = new TournamentSelection(1);
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            ts.select(null);
        });
    }

    @Test
    @DisplayName("Lanza excepción si la población está vacía")
    public void selectThrowsWhenPopulationIsEmpty() throws EvolutionaryAlgorithmException {
        TournamentSelection ts = new TournamentSelection(1);
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            ts.select(new int[]{});
        });
    }

    @Test
    @DisplayName("Lanza excepción si la población tiene igual o menos elementos que el tamaño del torneo")
    public void selectThrowsWhenPopulationIsSmallerOrEqualThanTournamentSize() throws EvolutionaryAlgorithmException {
        TournamentSelection ts = new TournamentSelection(3);
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            ts.select(new int[]{5, 3, 1});
        });
    }

    @Test
    @DisplayName("Selecciona individuos correctamente cuando tournamentSize = 1")
    public void selectReturnsValidSelectionWithTournamentSizeOne() throws EvolutionaryAlgorithmException {
        TournamentSelection ts = new TournamentSelection(1);
        int[] population = {1, 2, 3};
        int[] selected = ts.select(population);

        assertNotNull(selected);
        assertEquals(population.length, selected.length);
    }

    @Test
    @DisplayName("Selecciona los mejores candidatos en torneos de múltiples rondas")
    public void selectReturnsBestCandidatesWithTournamentSizeGreaterThanOne() throws EvolutionaryAlgorithmException {
        TournamentSelection ts = new TournamentSelection(2);
        int[] population = {1, 3, 2, 5};
        int[] selected = ts.select(population);

        assertNotNull(selected);
        assertEquals(population.length, selected.length);
        for (int val : selected) {
            assertTrue(val >= 1 && val <= 5);
        }
    }
}
