package org.mps.mutation;

import java.util.Random;

import org.mps.EvolutionaryAlgorithmException;

/**

La clase GaussianMutation implementa el operador de mutación gaussiana en algoritmos evolutivos.
Este operador de mutación introduce cambios aleatorios en una solución candidata mediante una alteración
basada en una distribución normal.
En la mutación gaussiana, se recorre cada elemento de la solución y, con una probabilidad determinada
por la tasa de mutación (mutationRate), se aplica una variación aleatoria a su valor. Esta variación
es generada a partir de una distribución normal con media 0 y desviación estándar especificada
por el parámetro standardDeviation. El resultado es un cambio pequeño y aleatorio en el valor de
la solución, lo que permite explorar nuevas variaciones sin modificar su estructura básica.
El operador de mutación gaussiana es útil para introducir diversidad en la población de soluciones,
permitiendo una búsqueda más eficiente y evitando que el algoritmo evolutivo se quede atrapado
en óptimos locales. Al generar cambios suaves y controlados, fomenta la exploración de un espacio de
búsqueda más amplio y flexible.
 */
public class GaussianMutation implements MutationOperator {
    private Random random;
    private double mutationRate;
    private double standardDeviation;

    public GaussianMutation() {
        this.random = new Random();
        this.mutationRate = 0;
        this.standardDeviation = 0;
    }

    public GaussianMutation(double mutationRate, double standardDeviation) {
        this.random = new Random();
        this.mutationRate = mutationRate;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public int[] mutate(int[] individual) throws EvolutionaryAlgorithmException {
        if (individual == null || individual.length == 0) {
            throw new EvolutionaryAlgorithmException("No se puede realizar la mutación");
        }
        
        int[] mutatedIndividual = individual.clone();
        for (int i = 0; i < mutatedIndividual.length; i++) {
            if (random.nextDouble() < mutationRate) {
                mutatedIndividual[i] += Math.round((random.nextGaussian() * standardDeviation));
            }
        }
        
        return mutatedIndividual;
    }
}
