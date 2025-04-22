package org.mps.crossover;
import java.util.Random;

import org.mps.EvolutionaryAlgorithmException;

/**

La clase TwoPointCrossover implementa el operador de cruce de dos puntos en algoritmos evolutivos.
Este operador simula el proceso biológico de recombinación genética, donde dos soluciones (padres)
se combinan para generar dos nuevas soluciones (descendientes) mediante la intervención de dos puntos
de cruce aleatorios.
En el cruce de dos puntos, se seleccionan dos puntos aleatorios en las soluciones de los padres y
se intercambian segmentos de estas soluciones entre los dos puntos. Los segmentos antes del primer
punto de cruce se mantienen de los padres originales, el segmento entre los dos puntos de cruce se
intercambia entre los padres y los segmentos después del segundo punto de cruce también se mantienen.
Como resultado, se generan dos soluciones nuevas, cada una con características de ambos padres.
El operador de cruce de dos puntos permite explorar de manera efectiva el espacio de búsqueda,
combinando las características de ambos padres y creando variabilidad en las soluciones descendientes.
Este tipo de cruce es eficaz para mantener la diversidad genética y evitar la convergencia prematura
hacia una solución subóptima.
 */
public class TwoPointCrossover implements CrossoverOperator {
    private Random random;

    public TwoPointCrossover() {
        this.random = new Random();
    }

    @Override
    public int[][] crossover(int[] parent1, int[] parent2) throws EvolutionaryAlgorithmException {
        int[][] offspring = null;
        if (parent1 != null && parent2 != null && parent1.length > 1 && parent1.length == parent2.length) {
            offspring = new int[2][parent1.length];
            int point1 = random.nextInt(parent1.length - 1);
            int point2 = random.nextInt(parent1.length - point1 - 1) + point1 + 1;

            // Copiar segmentos antes del primer punto de cruce
            for (int i = 0; i < point1; i++) {
                offspring[0][i] = parent1[i];
                offspring[1][i] = parent2[i];
            }

            // Intercambiar segmentos entre los dos puntos de cruce
            for (int i = point1; i < point2; i++) {
                offspring[0][i] = parent2[i];
                offspring[1][i] = parent1[i];
            }

            // Copiar segmentos después del segundo punto de cruce
            for (int i = point2; i < parent1.length; i++) {
                offspring[0][i] = parent1[i];
                offspring[1][i] = parent2[i];
            }
        } else {
            throw new EvolutionaryAlgorithmException("No se ha podido realizar el cruce");
        }
        return offspring;
    }
}

