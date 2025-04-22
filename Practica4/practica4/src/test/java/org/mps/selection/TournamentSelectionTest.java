package org.mps.selection;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;


    
@Nested
@DisplayName("TournamentSelection")
public class TournamentSelectionTest {
    @Test
    public void constructor_NonPositiveSize_Throws() {
        assertThrows(EvolutionaryAlgorithmException.class, () -> new TournamentSelection(0));
    }

    @Test
    public void select_InvalidPopulation_Throws() throws Exception {
        TournamentSelection ts = new TournamentSelection(3);
        assertThrows(EvolutionaryAlgorithmException.class, () -> ts.select(new int[] { 1, 2 }));
    }

    @Test
    public void select_ValidPopulation_ReturnsSameLengthArray() throws Exception {
        int[] pop = { 1, 5, 3, 7, 2 };
        TournamentSelection ts = new TournamentSelection(2);
        int[] selected = ts.select(pop);
        assertEquals(pop.length, selected.length);
        for (int s : selected) {
            assertTrue(s >= 1 && s <= 7, "Selected value must come from the original population");
        }
    }
}

