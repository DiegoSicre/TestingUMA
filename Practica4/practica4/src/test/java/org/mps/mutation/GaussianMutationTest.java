package org.mps.mutation;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;


@Nested
@DisplayName("GaussianMutation")
public class GaussianMutationTest {
    @Test
    public void mutate_NullIndividual_Throws() {
        GaussianMutation gm = new GaussianMutation(1.0, 1.0);
        assertThrows(EvolutionaryAlgorithmException.class, () -> gm.mutate(null));
    }

    @Test
    public void mutate_RateZero_ReturnsIdenticalArray() throws Exception {
        int[] ind = { 1, 2, 3 };
        GaussianMutation gm = new GaussianMutation(0.0, 1.0);
        int[] mutated = gm.mutate(ind);
        assertArrayEquals(ind, mutated);
    }
}