package org.mps.selection;

import java.util.Random;

import org.mps.EvolutionaryAlgorithmException;

/**
 * La clase TournamentSelection implementa el operador de selección por torneo
 * en algoritmos evolutivos.
 * Este operador de selección simula el proceso de competición entre individuos
 * de una población para determinar los progenitores de la próxima generación.
 *
 * En la selección por torneo, se elige aleatoriamente un subconjunto de la
 * población, conocido como "torneo". Luego, se selecciona al individuo más apto
 * dentro de este torneo para ser utilizado como progenitor.
 *
 * La principal ventaja de la selección por torneo es su simplicidad y
 * eficiencia computacional. Además, permite controlar el tamaño del torneo, lo
 * que puede afectar la presión selectiva y la diversidad genética en la
 * población.
 *
 */

public class TournamentSelection implements SelectionOperator {
    private Random random = new Random();
    private int tournamentSize;

    public TournamentSelection(int tournamentSize) throws EvolutionaryAlgorithmException {
        if (tournamentSize > 0)
            this.tournamentSize = tournamentSize;
        else
            throw new EvolutionaryAlgorithmException("El tamanyo del torneo debe ser mayor que cero");

    }

    @Override
    public int[] select(int[] population) throws EvolutionaryAlgorithmException {
        int[] selected;
        if (population != null && population.length > 0 && population.length > tournamentSize) {
            selected = new int[population.length];
            for (int i = 0; i < population.length; i++) {
                int best = -1;
                for (int j = 0; j < tournamentSize; j++) {
                    int candidate = population[random.nextInt(population.length)];
                    if (best == -1 || candidate > best) {
                        best = candidate;
                    }
                }
                selected[i] = best;
            }
        } else {
            throw new EvolutionaryAlgorithmException("No se ha podido realizar la selección");
        }
        return selected;
    }
}
