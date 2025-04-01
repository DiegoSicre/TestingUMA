package binarySearchTree;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la clase BinarySearchTree.
 * - Estilo BDD con anotaciones @Nested y @DisplayName
 * - Patrón AAA (Arrange - Act - Assert)
 * - Cobertura total de líneas con JaCoCo
 * - Pruebas independientes entre sí
 */
@DisplayName("BinarySearchTree Tests")
public class BinarySearchTreeTest {

    Comparator<Integer> comparator = Integer::compareTo;

    @Nested
    @DisplayName("Insert Tests")
    class InsertTests {

        @Test
        @DisplayName("Insert into empty tree should set root value")
        void insertIntoEmptyTree_setsRootValue() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);

            // Act
            tree.insert(10);

            // Assert
            assertTrue(tree.contains(10));
            assertEquals(1, tree.size());
        }

        @Test
        @DisplayName("Insert multiple values should respect BST ordering")
        void insertMultipleValues_createsValidStructure() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);

            // Act
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);

            // Assert
            assertTrue(tree.contains(10));
            assertTrue(tree.contains(5));
            assertTrue(tree.contains(15));
            assertFalse(tree.contains(20));
        }
    }

    @Nested
    @DisplayName("Size and Depth Tests")
    class SizeDepthTests {

        @Test
        @DisplayName("Size and depth of empty tree should be 0")
        void emptyTree_hasSizeAndDepthZero() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);

            // Act & Assert
            assertEquals(0, tree.size());
            assertEquals(0, tree.depth());
        }

        @Test
        @DisplayName("Size and depth after several inserts")
        void sizeAndDepthAfterInsertions() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);
            tree.insert(10);
            tree.insert(5);
            tree.insert(3);
            tree.insert(15);

            // Act & Assert
            assertEquals(4, tree.size());
            assertEquals(3, tree.depth());
        }
    }

    @Nested
    @DisplayName("Minimum and Maximum Tests")
    class MinMaxTests {

        @Test
        @DisplayName("Minimum and maximum in a filled tree")
        void minMaxCorrectness() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);
            tree.insert(10);
            tree.insert(2);
            tree.insert(20);
            tree.insert(1);
            tree.insert(25);

            // Act & Assert
            assertEquals(1, tree.minimum());
            assertEquals(25, tree.maximum());
        }
    }

    @Nested
    @DisplayName("RemoveBranch Tests")
    class RemoveBranchTests {

        @Test
        @DisplayName("RemoveBranch elimina nodo profundo en la rama izquierda")
        void removeBranch_removesDeepLeftSubtree() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);
            tree.insert(10);
            tree.insert(5);
            tree.insert(4); // Subnodo en la izquierda profunda
            tree.insert(15);

            // Act
            tree.removeBranch(4);

            // Assert
            assertFalse(tree.contains(4));
            assertTrue(tree.contains(5));
            assertTrue(tree.contains(10));
            assertTrue(tree.contains(15));
        }
        @Test
        @DisplayName("RemoveBranch elimina nodo profundo en la rama derecha")
        void removeBranch_removesDeepRightSubtree() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);
            tree.insert(10);
            tree.insert(15);
            tree.insert(20); // Subnodo en la derecha profunda
            tree.insert(5);

            // Act
            tree.removeBranch(20);

            // Assert
            assertFalse(tree.contains(20));
            assertTrue(tree.contains(15));
            assertTrue(tree.contains(10));
            assertTrue(tree.contains(5));
        }
        
        @Test
        @DisplayName("RemoveBranch en árbol vacío no lanza excepción y no hace nada")
        void removeBranch_onEmptyTree_doesNothing() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator); // árbol vacío

            // Act & Assert
            assertDoesNotThrow(() -> tree.removeBranch(42));
            assertEquals(0, tree.size()); // sigue vacío
        }

        
        @Test
        @DisplayName("Removing a branch should remove entire subtree")
        void removeBranch_removesSubtree() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);
            tree.insert(10);
            tree.insert(5);
            tree.insert(3);
            tree.insert(7);
            tree.insert(15);

            // Act
            tree.removeBranch(5);

            // Assert
            assertFalse(tree.contains(5));
            assertFalse(tree.contains(3));
            assertFalse(tree.contains(7));
            assertTrue(tree.contains(10));
            assertTrue(tree.contains(15));
        }

        @Test
        @DisplayName("Removing non-existing value should not crash")
        void removeBranch_nonExistingValue_doesNothing() {
            // Arrange
            BinarySearchTree<Integer> tree = new BinarySearchTree<>(comparator);
            tree.insert(10);
            tree.insert(5);

            // Act
            tree.removeBranch(999); // does not exist

            // Assert
            assertEquals(2, tree.size());
        }
    }
}
