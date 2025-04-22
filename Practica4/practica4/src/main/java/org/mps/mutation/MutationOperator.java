package org.mps.mutation;

import org.mps.EvolutionaryAlgorithmException;

/**
 * La interfaz MutationOperator representa un operador utilizado en algoritmos
 * evolutivos para realizar la mutación de soluciones candidatas.
 * Este operador simula el proceso biológico de mutación genética, donde se
 * introducen cambios aleatorios en las soluciones para explorar nuevas regiones
 * del espacio de búsqueda.
 *
 * Los operadores de mutación se aplican a soluciones candidatas seleccionadas
 * de la población actual. Estos operadores pueden cambiar uno o más componentes
 * de las soluciones, como valores de genes o estructuras completas, con cierta
 * probabilidad.
 *
 * La mutación es importante en los algoritmos evolutivos porque introduce
 * variabilidad en la población y ayuda a evitar la convergencia prematura hacia
 * óptimos locales. También puede permitir la exploración de regiones del
 * espacio de búsqueda que de otro modo no serían accesibles.
 *
 * La implementación concreta de un operador de mutación depende del problema a
 * resolver y de las características de las soluciones candidatas. Es común que
 * los operadores de mutación sean diseñados específicamente para adaptarse a
 * las particularidades del problema en cuestión.
 *
 * La interfaz MutationOperator define el contrato que deben cumplir todas las
 * implementaciones de operadores de mutación, permitiendo la flexibilidad y la
 * personalización en el diseño de algoritmos evolutivos.
 */
public interface MutationOperator {
    int[] mutate(int[] population) throws EvolutionaryAlgorithmException;
}
