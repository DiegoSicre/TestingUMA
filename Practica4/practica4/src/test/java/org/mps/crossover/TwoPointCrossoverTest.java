package org.mps.crossover;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;



@DisplayName("TwoPointCrossover")
public class TwoPointCrossoverTest {
    private final TwoPointCrossover crossover = new TwoPointCrossover();

    @Test
    public void crossover_ValidParents_ReturnsTwoOffspring() throws Exception {
        int[] p1 = { 1, 2 };
        int[] p2 = { 3, 4 };
        int[][] off = crossover.crossover(p1, p2);
        assertEquals(2, off.length);
        assertEquals(p1.length, off[0].length);
        // For length 2 parents, crossover is deterministic: point1=0, point2=1
        assertArrayEquals(new int[] { 3, 2 }, off[0]);
        assertArrayEquals(new int[] { 1, 4 }, off[1]);
    }

    @Test
    public void crossover_MismatchedLength_Throws() {
        assertThrows(EvolutionaryAlgorithmException.class,
                () -> crossover.crossover(new int[] { 1, 2 }, new int[] { 3 }));
    }
}
