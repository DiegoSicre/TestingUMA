package org.mps.boundedqueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class ArrayBoundedQueueTest {


    @Test
    @DisplayName("Lanza excepción cuando la capacidad es 0")
    void shouldThrowWhenCapacityIsZero() {
        // Act & Assert
        assertThatThrownBy(() -> new ArrayBoundedQueue<>(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity must be positive");
    }

    @Test
    @DisplayName("Lanza excepción cuando la capacidad es negativa")
    void shouldThrowWhenCapacityIsNegative() {
        // Act & Assert
        assertThatThrownBy(() -> new ArrayBoundedQueue<>(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity must be positive");
    }


    @Test
    @DisplayName("Lanza excepción al insertar null")
    void shouldThrowWhenPutNull() {
        // Arrange
        ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(1);

        // Act & Assert
        assertThatThrownBy(() -> q.put(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("element cannot be null");
    }

    @Test
    @DisplayName("Lanza excepción al insertar en cola llena")
    void shouldThrowWhenPutOnFullQueue() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(1);
        q.put(1);

        // Act & Assert
        assertThatThrownBy(() -> q.put(2))
                .isInstanceOf(FullBoundedQueueException.class)
                .hasMessageContaining("full bounded queue");
    }


    @Test
    @DisplayName("Lanza excepción al extraer de cola vacía")
    void shouldThrowWhenGetOnEmptyQueue() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(1);

        // Act & Assert
        assertThatThrownBy(q::get)
                .isInstanceOf(EmptyBoundedQueueException.class)
                .hasMessageContaining("empty bounded queue");
    }

    @Test
    @DisplayName("Extrae un elemento manteniendo el orden FIFO")
    void shouldReturnElementsInFifoOrder() {
        // Arrange
        ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(3);
        q.put("A");
        q.put("B");
        q.put("C");

        // Act
        String first = q.get();

        // Assert
        assertThat(first).isEqualTo("A");
        assertThat(q.size()).isEqualTo(2);
        assertThat(q.isEmpty()).isFalse();
    }


    @Test
    @DisplayName("isFull devuelve true cuando la cola está llena")
    void isFullReturnsTrueWhenQueueIsFull() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);
        q.put(1);
        q.put(2);

        // Act & Assert
        assertThat(q.isFull()).isTrue();
    }

    @Test
    @DisplayName("isEmpty devuelve true cuando la cola está vacía")
    void isEmptyReturnsTrueWhenQueueIsEmpty() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);

        // Act & Assert
        assertThat(q.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("size refleja el número real de elementos")
    void sizeReflectsActualNumberOfElements() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(3);
        q.put(1);
        q.put(2);

        // Act & Assert
        assertThat(q.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("getFirst y getLast se actualizan correctamente con wrap-around")
    void firstAndLastIndicesWrapAroundCorrectly() {
        // Arrange
        ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(2);
        q.put(10);     // first = 0, nextFree = 1
        q.put(20);     // first = 0, nextFree = 0 (envuelto)

        q.get();       // first = 1

        // Act
        int firstIndex  = q.getFirst(); // debería ser 1
        int lastIndex   = q.getLast();  // debería ser 0

        // Assert
        assertThat(firstIndex).isEqualTo(1);
        assertThat(lastIndex).isEqualTo(0);
    }


    @Nested
    @DisplayName("Iterator")
    class IteratorBehaviour {

        @Test
        @DisplayName("Recorre los elementos en orden FIFO")
        void iteratesOverElementsInFifoOrder() {
            // Arrange
            ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(3);
            q.put("uno");
            q.put("dos");
            q.put("tres");

            // Act
            Iterator<String> it = q.iterator();

            // Assert
            assertThat(it.hasNext()).isTrue();
            assertThat(it.next()).isEqualTo("uno");
            assertThat(it.next()).isEqualTo("dos");
            assertThat(it.next()).isEqualTo("tres");
            assertThat(it.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Lanza excepción si se llama next() sin elementos restantes")
        void throwsWhenNextCalledAfterExhaustion() {
            // Arrange
            ArrayBoundedQueue<Integer> q = new ArrayBoundedQueue<>(1);
            q.put(7);
            Iterator<Integer> it = q.iterator();
            it.next(); // consume el único elemento

            // Act & Assert
            assertThat(it.hasNext()).isFalse();
            assertThatThrownBy(it::next)
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("iterator exhausted");
        }
    }
}
