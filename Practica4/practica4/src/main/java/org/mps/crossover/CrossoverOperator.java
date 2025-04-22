package org.mps.crossover;

import org.mps.EvolutionaryAlgorithmException;

/**
 * La interfaz CrossoverOperator representa un operador utilizado en algoritmos
 * evolutivos para realizar el cruce o recombinación de soluciones candidatas.
 * Este operador simula el proceso biológico de reproducción, donde se combinan
 * características de dos padres para producir descendientes con nuevas
 * características.
 *
 * Los operadores de cruce definen cómo se realizan las combinaciones entre los
 * padres para generar nuevos individuos. Existen varios tipos de operadores de
 * cruce, como el cruce de un punto, el cruce de dos puntos, el cruce uniforme,
 * entre otros.
 *
 * El proceso de cruce típicamente implica seleccionar aleatoriamente puntos de
 * cruce en las soluciones de los padres y luego intercambiar segmentos de las
 * soluciones para producir descendientes.
 *
 * La implementación concreta de un operador de cruce depende del problema a
 * resolver y de las características de las soluciones candidatas. Es común que
 * los operadores de cruce sean diseñados de manera específica para adaptarse a
 * las particularidades del problema en cuestión.
 *
 * La interfaz CrossoverOperator define el contrato que deben cumplir todas las
 * implementaciones de operadores de cruce, permitiendo la flexibilidad y la
 * personalización en el diseño de algoritmos evolutivos.
 */

public interface CrossoverOperator {
    public int[][] crossover(int[] parent1, int[] parent2) throws EvolutionaryAlgorithmException;
}
