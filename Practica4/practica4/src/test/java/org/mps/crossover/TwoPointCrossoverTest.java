package org.mps.crossover;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;

public class TwoPointCrossoverTest {

    private TwoPointCrossover twoPointCrossover;

    @BeforeEach
    public void setUp() {
        // Inicializamos la clase TwoPointCrossover
        twoPointCrossover = new TwoPointCrossover();
    }

    @Test
    @DisplayName("Verifica el cruce de dos padres válidos")
    public void testCrossoverWithValidParents() throws EvolutionaryAlgorithmException {
        // Arrange
        int[] parent1 = {1, 2, 3, 4, 5};
        int[] parent2 = {5, 4, 3, 2, 1};

        // Act
        int[][] offspring = twoPointCrossover.crossover(parent1, parent2);

        // Assert
        assertNotNull(offspring);
        assertEquals(parent1.length, offspring[0].length);
        assertEquals(parent2.length, offspring[1].length);
    }

    @Test
    @DisplayName("Verifica que se lanza una excepción si el padre 1 es nulo")
    public void testCrossoverWithNullParent1() {
        // Arrange
        int[] parent1 = {1, 2, 3, 4, 5};
        int[] parent2 = null;

        // Act & Assert
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            twoPointCrossover.crossover(parent1, parent2);
        });
    }
    @Test
    @DisplayName("Verifica que se lanza una excepción si el padre 2 es nulo")
    public void testCrossoverWithNullParent2() {
        // Arrange
        int[] parent1 = null;
        int[] parent2 = {1, 2, 3, 4, 5};

        // Act & Assert
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            twoPointCrossover.crossover(parent1, parent2);
        });
    }
    @Test
    @DisplayName("Verifica que se lanza una excepción si los padres tienen longitudes diferentes")
    public void testCrossoverWithDifferentLengthParents() {
        // Arrange
        int[] parent1 = {1, 2, 3, 4, 5};
        int[] parent2 = {5, 4, 3};
    
        // Act & Assert
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            twoPointCrossover.crossover(parent1, parent2);
        });
    }

    @Test
    @DisplayName("Verifica que se lanza una excepción si los padres tienen longitud 1")
    public void testCrossoverWithSingleElementParents() {
        // Arrange
        int[] parent1 = {1};
        int[] parent2 = {2};
    
        // Act & Assert
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            twoPointCrossover.crossover(parent1, parent2);
        });
    }

}
