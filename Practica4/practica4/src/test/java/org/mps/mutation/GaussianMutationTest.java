package org.mps.mutation;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;

public class GaussianMutationTest {
    @Test
    @DisplayName("Verifica que la mutación se aplica correctamente a un individuo válido")
    public void testMutateWithValidIndividual() throws EvolutionaryAlgorithmException {
        // Arrange
        int[] individual = {1, 2, 3, 4, 5};
        GaussianMutation gaussianMutation = new GaussianMutation(1.0, 1.0); // Tasa de mutación 100%

        // Act
        int[] mutatedIndividual = gaussianMutation.mutate(individual);

        // Assert
        assertNotNull(mutatedIndividual);
        assertEquals(individual.length, mutatedIndividual.length);
        assertFalse(Arrays.equals(individual, mutatedIndividual));
    }
   
    @Test
    @DisplayName("Verifica que se lanza una excepción si el individuo es nulo")
    public void testMutateWithNullIndividual() {
        // Arrange
        int[] individual = null;
        GaussianMutation gaussianMutation = new GaussianMutation(0.5, 1.0);

        // Act & Assert
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            gaussianMutation.mutate(individual);
        });
    }

    @Test
    @DisplayName("Verifica que se lanza una excepción si el individuo tiene longitud 0")
    public void testMutateWithEmptyIndividual() {
        // Arrange
        int[] individual = {};
        GaussianMutation gaussianMutation = new GaussianMutation(0.5, 1.0);

        // Act & Assert
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            gaussianMutation.mutate(individual);
        });
    }

    @Test
    @DisplayName("Verifica que no se aplica mutación cuando la tasa de mutación es 0")
    public void testMutateWithZeroMutationRate() throws EvolutionaryAlgorithmException {
        // Arrange
        int[] individual = {1, 2, 3, 4, 5};
        GaussianMutation gaussianMutation = new GaussianMutation(); // Tasa de mutación 0%

        // Act
        int[] mutatedIndividual = gaussianMutation.mutate(individual);

        // Assert
        assertArrayEquals(individual, mutatedIndividual);
    }

    @Test
    @DisplayName("Verifica que siempre se aplica la mutación cuando la tasa de mutación es 1")
    public void testMutateWithOneMutationRate() throws EvolutionaryAlgorithmException {
        // Arrange
        int[] individual = {1, 2, 3, 4, 5};
        GaussianMutation gaussianMutation = new GaussianMutation(1.0, 1.0); // Tasa de mutación 100%

        // Act
        int[] mutatedIndividual = gaussianMutation.mutate(individual);

        // Assert
        assertFalse(Arrays.equals(individual, mutatedIndividual));
    }

}
