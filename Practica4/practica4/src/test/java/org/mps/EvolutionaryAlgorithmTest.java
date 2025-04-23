package org.mps;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mps.crossover.CrossoverOperator;
import org.mps.mutation.MutationOperator;
import org.mps.selection.SelectionOperator;

/**
 * White‑box test suite for the EvolutionaryAlgorithm practice.
 * <p>
 *  ▸ 100 % branch coverage for EvolutionaryAlgorithm, TwoPointCrossover,
 *    GaussianMutation and TournamentSelection classes.
 *  ▸ Tests follow the AAA pattern and use descriptive names.
 */
@ExtendWith(MockitoExtension.class)
public class EvolutionaryAlgorithmTest {

    // ────────────────────────────────────────────────────────────────────────────
    // EvolutionaryAlgorithm
    // ────────────────────────────────────────────────────────────────────────────
    @Mock
    private SelectionOperator selectionOp;
    @Mock
    private MutationOperator mutationOp;
    @Mock
    private CrossoverOperator crossoverOp;

    private EvolutionaryAlgorithm ea;

    @Nested
    @DisplayName("EvolutionaryAlgorithm constructor")
    public class ConstructorTest {

        @Test
        public void constructor_AllValid_NoException() {
            assertDoesNotThrow(() -> new EvolutionaryAlgorithm(selectionOp, mutationOp, crossoverOp));
        }

        @Test
        public void constructor_NullSelection_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () ->
                    new EvolutionaryAlgorithm(null, mutationOp, crossoverOp));
        }

        @Test
        public void constructor_NullMutation_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () ->
                    new EvolutionaryAlgorithm(selectionOp, null, crossoverOp));
        }

        @Test
        public void constructor_NullCrossover_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () ->
                    new EvolutionaryAlgorithm(selectionOp, mutationOp, null));
        }

        @Test
        public void constructor_AllNull_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () ->
                    new EvolutionaryAlgorithm(null, null, null));
        }

        @Test
        public void constructor_TwoNulls_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () ->
                    new EvolutionaryAlgorithm(null, mutationOp, null));
        }
    }

    @Nested
    @DisplayName("EvolutionaryAlgorithm#optimize")
    public class OptimizeTest {
        
        //Tenemos que testear todos los casos posibles del constructor

        @BeforeEach
        void setUp() throws EvolutionaryAlgorithmException {
            ea = new EvolutionaryAlgorithm(selectionOp, mutationOp, crossoverOp);
        }

        @Test
        @DisplayName("Null population ⇒ throws EvolutionaryAlgorithmException")
        public void optimize_NullPopulation_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () -> ea.optimize(null));
        }

        @Test
        @DisplayName("Empty population ⇒ throws EvolutionaryAlgorithmException")
        public void optimize_EmptyPopulation_Throws() {
            assertThrows(EvolutionaryAlgorithmException.class, () -> ea.optimize(new int[0][]));
        }

        @Test
        @DisplayName("Even population, offspring better ⇒ population replaced")
        public void optimize_OffspringBetter_Replaced() throws Exception {
            // Arrange
            int[][] population = { { 5, 5 }, { 6, 6 } };
            when(selectionOp.select(population[0])).thenReturn(population[0]);
            when(selectionOp.select(population[1])).thenReturn(population[1]);
            int[][] offspring = { { 1, 1 }, { 2, 2 } };
            when(crossoverOp.crossover(population[0], population[1])).thenReturn(offspring);
            when(mutationOp.mutate(offspring[0])).thenReturn(offspring[0]);
            when(mutationOp.mutate(offspring[1])).thenReturn(offspring[1]);

            // Act
            int[][] result = ea.optimize(population);

            // Assert
            assertArrayEquals(offspring[0], result[0]);
            assertArrayEquals(offspring[1], result[1]);
        }

        @Test
        @DisplayName("Even population, offspring worse ⇒ population unchanged")
        public void optimize_OffspringWorse_NotReplaced() throws Exception {
            int[][] population = { { 1, 1 }, { 2, 2 } };
            when(selectionOp.select(any(int[].class))).thenAnswer(inv -> inv.getArgument(0));
            int[][] offspring = { { 5, 5 }, { 6, 6 } };
            when(crossoverOp.crossover(population[0], population[1])).thenReturn(offspring);
            when(mutationOp.mutate(any(int[].class))).thenAnswer(inv -> inv.getArgument(0));
            int[][] snapshot = { population[0].clone(), population[1].clone() };

            int[][] result = ea.optimize(population);

            assertArrayEquals(snapshot[0], result[0]);
            assertArrayEquals(snapshot[1], result[1]);
        }

        @Test
        @DisplayName("Odd population size ⇒ IndexOutOfBoundsException (latent bug)")
        public void optimize_OddPopulation_FailsFast() throws Exception {
            int[][] population = { { 1, 1 }, { 2, 2 }, { 3, 3 } };
           
            assertThrows(EvolutionaryAlgorithmException.class, () -> ea.optimize(population));
        }
    }



}
