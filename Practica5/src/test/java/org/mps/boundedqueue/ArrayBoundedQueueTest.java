package org.mps.boundedqueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class ArrayBoundedQueueTest {


    /* ---------------------------------------------------------
     *  Constructor
     * --------------------------------------------------------- */
    @Test
    @DisplayName("Lanza excepción cuando la capacidad es 0")
    public void constructor_capacityZero_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ArrayBoundedQueue<>(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity must be positive");
    }

    @Test
    @DisplayName("Lanza excepción cuando la capacidad es negativa")
    public void constructor_negativeCapacity_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ArrayBoundedQueue<>(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity must be positive");
    }
    @Test
    @DisplayName("Constructor con capacidad positiva crea cola vacía")
    public void constructor_positiveCapacity_createsEmptyQueue() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(3);

        assertThat(q.isEmpty()).isTrue();
        assertThat(q.size()).isZero();
        assertThat(q.isFull()).isFalse();
    }


    /* ---------------------------------------------------------
     *  put
     * --------------------------------------------------------- */
    @Test
    @DisplayName("Lanza excepción al insertar null")
    public void put_null_throwsIllegalArgumentException() {
        ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(1);

        assertThatThrownBy(() -> q.put(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("element cannot be null");
    }

    @Test
    @DisplayName("Lanza excepción al insertar en cola llena")
    public void put_whenFull_throwsFullBoundedQueueException() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(1);
        q.put(1);

        assertThatThrownBy(() -> q.put(2))
                .isInstanceOf(FullBoundedQueueException.class)
                .hasMessageContaining("full bounded queue");
    }



    /* ---------------------------------------------------------
     *  get
     * --------------------------------------------------------- */
    @Test
    @DisplayName("Lanza excepción al extraer de cola vacía")
    public void get_whenEmpty_throwsEmptyBoundedQueueException() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(1);

        assertThatThrownBy(q::get)
                .isInstanceOf(EmptyBoundedQueueException.class)
                .hasMessageContaining("empty bounded queue");
    }

    @Test
    @DisplayName("Extrae un elemento manteniendo el orden FIFO")
    public void get_afterMultipleInserts_returnsFirstElementAndMaintainsFIFO() {
        ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(3);
        q.put("A");
        q.put("B");
        q.put("C");

        String first = q.get();

        assertThat(first).isEqualTo("A");
        assertThat(q.size()).isEqualTo(2);
        assertThat(q.isEmpty()).isFalse();
        assertThat(q.isFull()).isFalse();
    }
    /* ------------------------------------------------------------------
    * Wrap‑around completo: insertar‑sacar‑insertar
    * ------------------------------------------------------------------ */

    /** Permite volver a insertar tras liberar espacio y mantiene orden FIFO correcto */
    @Test
    @DisplayName("Mantiene orden FIFO tras wrap‑around y nueva inserción")
    public void maintainsFifoOrderAfterWrapAroundReinsertion() {
        // Arrange  (capacidad 2 para provocar wrap‑around rápido)
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);
        q.put(10);
        q.put(20);        // llena, nextFree envuelto a 0

        q.get();          // first avanza a 1, deja hueco en 0
        q.put(30);        // se inserta en posición 0 (wrap‑around completo)

        // Act & Assert
        assertThat(q.get()).isEqualTo(20);
        assertThat(q.get()).isEqualTo(30);
        assertThat(q.isEmpty()).isTrue();
    }

    /* ---------------------------------------------------------
     *  isFull / isEmpty / size
     * --------------------------------------------------------- */
    @Test
    @DisplayName("isFull devuelve true cuando la cola está llena")
    public void isFull_whenFull_returnsTrue() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);
        q.put(1);
        q.put(2);

        assertThat(q.isFull()).isTrue();
    }

    @Test
    @DisplayName("isEmpty devuelve true cuando la cola está vacía")
    public void isEmpty_whenEmpty_returnsTrue() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);

        assertThat(q.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("size refleja el número real de elementos")
    public void size_afterTwoInserts_returns2() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(3);
        q.put(1);
        q.put(2);

        assertThat(q.size()).isEqualTo(2);
    }


    /** isFull vuelve a false tras sacar un elemento de una cola que estaba llena */
    @Test
    @DisplayName("isFull vuelve a false después de hacer get() en cola llena")
    public void isFull_AfterRemovingFromFullQueue_ReturnsFalse() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);
        q.put(1);
        q.put(2);                    // llena

        // Act
        q.get();                     // libera un hueco

        // Assert
        assertThat(q.isFull()).isFalse();
    }

    /** isEmpty vuelve a true tras extraer todos los elementos */
    @Test
    @DisplayName("isEmpty vuelve a true después de extraer todos los elementos")
    public void isEmpty_AfterRemovingAllElements_ReturnsTrue() {
        // Arrange
        ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(1);
        q.put("X");

        // Act
        q.get();                     // vacía la cola

        // Assert
        assertThat(q.isEmpty()).isTrue();
        assertThat(q.size()).isZero();
    }

    /* ---------------------------------------------------------
     *  Índices internos
     * --------------------------------------------------------- */
    @Test
    @DisplayName("getFirst y getLast se actualizan correctamente con wrap-around")
    public void getFirstAndGetLast_afterWrapAround_returnUpdatedIndices() {
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);
        q.put(10);     // first = 0, nextFree = 1
        q.put(20);     // first = 0, nextFree = 0 (wrap)
        q.get();       // first = 1

        int firstIndex = q.getFirst();
        int lastIndex  = q.getLast();

        assertThat(firstIndex).isEqualTo(1);
        assertThat(lastIndex).isEqualTo(0);
    }
    /* ---------------------------------------------------------
     *  Iterator
     * --------------------------------------------------------- */
    @Nested
    @DisplayName("Iterator")
    public class IteratorBehaviour {

        @Test
        @DisplayName("Recorre los elementos en orden FIFO")
        public void iterator_overFilledQueue_returnsElementsInFifoOrder() {
            ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(3);
            q.put("uno");
            q.put("dos");
            q.put("tres");

            Iterator<String> it = q.iterator();

            assertThat(it.hasNext()).isTrue();
            assertThat(it.next()).isEqualTo("uno");
            assertThat(it.next()).isEqualTo("dos");
            assertThat(it.next()).isEqualTo("tres");
            assertThat(it.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Lanza excepción si se llama next() sin elementos restantes")
        public void iterator_nextAfterExhaustion_throwsNoSuchElementException() {
            ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(1);
            q.put(7);
            Iterator<Integer> it = q.iterator();
            it.next(); // consume el único elemento

            assertThat(it.hasNext()).isFalse();
            assertThatThrownBy(it::next)
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("iterator exhausted");
        }
            /* ------------------------------------------------------------------
    * Iterador con layout circular
    * ------------------------------------------------------------------ */

    /** El iterador recorre correctamente cuando los datos están partidos en el buffer */
        @Test
        @DisplayName("Iterator recorre en orden FIFO con datos partidos (wrap‑around)")
        public void iterator_AfterWrapAround_TraversesCorrectly() {
            // Arrange  (capacidad 3, se parte en dos segmentos internos)
            ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(3);
            q.put("A"); q.put("B"); q.put("C");   // llena (first=0,nextFree=0)
            q.get();                              // saca "A" (first=1)
            q.put("D");                           // inserta en 0 (nextFree=1) → orden lógico: B,C,D

            // Act
            Iterator<String> it = q.iterator();

            // Assert
            assertThat(it).toIterable().containsExactly("B", "C", "D");
        }
    }



}
