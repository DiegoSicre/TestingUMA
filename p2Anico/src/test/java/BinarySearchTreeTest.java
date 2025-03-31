import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Tests para BinarySearchTree")
class BinarySearchTreeTest {

    BinarySearchTree<Integer> tree;

    @Nested
    @DisplayName("Cuando el árbol está vacío")
    class WhenEmpty {

        @BeforeEach
        void setUp() {
            tree = new BinarySearchTree<>();
        }

        @Test
        @DisplayName("size() devuelve 0")
        void sizeReturnsZero() {
            assertEquals(0, tree.size());
        }

        @Test
        @DisplayName("isleaf() lanza excepcion")
        void isleafReturnException() {
            assertThrows(BinarySearchTreeException.class, () -> {tree.isLeaf();});
        }

        @Test
        @DisplayName("depth() devuelve 0")
        void depthReturnsZero() {
            assertEquals(0, tree.depth());
        }

        @Test
        @DisplayName("minimum() lanza excepción")
        void minimumThrowsException() {
            assertThrows(BinarySearchTreeException.class, () -> tree.minimum());
        }

        @Test
        @DisplayName("maximum() lanza excepción")
        void maximumThrowsException() {
            assertThrows(BinarySearchTreeException.class, () -> tree.maximum());
        }

        @Nested
        @DisplayName("Después de insertar un valor")
        class AfterOneInsert {

            @BeforeEach
            void insertOne() {
                tree.insert(10);
            }

            @Test
            @DisplayName("size() devuelve 1")
            void sizeIsOne() {
                assertEquals(1, tree.size());
            }

            @Test
            @DisplayName("isLeaf() devuelve true")
            void isLeafIsTrue() {
                assertTrue(tree.isLeaf());
            }

            @Test
            @DisplayName("contains() encuentra el valor")
            void containsReturnsTrue() {
                assertTrue(tree.contains(10));
            }

            @Test
            @DisplayName("minimum() y maximum() devuelven el mismo valor")
            void minAndMaxAreSame() {
                assertEquals(10, tree.minimum());
                assertEquals(10, tree.maximum());
            }

            @Nested
            @DisplayName("Después de insertar más valores")
            class AfterMultipleInserts {

                @BeforeEach
                void insertMore() {
                    tree.insert(5);
                    tree.insert(15);
                    tree.insert(3);
                    tree.insert(7);
                }

                @Test
                @DisplayName("size() es correcto")
                void sizeIsCorrect() {
                    assertEquals(5, tree.size());
                }

                @Test
                @DisplayName("isLeaf() devuelve false si solo hay hijo izquierdo")
                void isLeaf_onlyLeftChild_returnsFalse() {
                    tree = new BinarySearchTree<>();
                    tree.insert(10);
                    tree.insert(5);
                    assertFalse(tree.isLeaf());
                }

                @Test
                @DisplayName("isLeaf() devuelve false si solo hay hijo derecho")
                void isLeaf_onlyRightChild_returnsFalse() {
                    tree = new BinarySearchTree<>();
                    tree.insert(10);
                    tree.insert(15);
                    assertFalse(tree.isLeaf());
                }

                @Test
                @DisplayName("contains() detecta valores existentes y no existentes")
                void containsWorks() {
                    assertTrue(tree.contains(3));
                    assertFalse(tree.contains(99));
                }

                @Test
                @DisplayName("minimum() devuelve el menor valor")
                void minimumIsCorrect() {
                    assertEquals(3, tree.minimum());
                }

                @Test
                @DisplayName("maximum() devuelve el mayor valor")
                void maximumIsCorrect() {
                    assertEquals(15, tree.maximum());
                }

                @Test
                @DisplayName("inOrder() devuelve lista ordenada")
                void inOrderReturnsSortedList() {
                    assertEquals(List.of(3, 5, 7, 10, 15), tree.inOrder());
                }

                @Test
                @DisplayName("depth() es correcta para árbol desbalanceado")
                void depthIsCorrect() {
                    assertEquals(3, tree.depth());
                }

                @Test
                @DisplayName("Insertar valor duplicado no aumenta tamaño")
                void insertDuplicate_doesNotIncreaseSize() {
                    int before = tree.size();
                    tree.insert(10);
                    assertEquals(before, tree.size());
                }

                @Test
                @DisplayName("Insertar null lanza excepción")
                void insertNull_throwsException() {
                    assertThrows(IllegalArgumentException.class, () -> tree.insert(null));
                }

                @Test
                @DisplayName("isLeaf() devuelve false si el nodo raíz tiene hijos")
                void isLeaf_rootHasChildren_returnsFalse() {
                    assertFalse(tree.isLeaf());
                }

                @Test
                @DisplayName("contains(null) devuelve false sin lanzar excepción")
                void containsNull_returnsFalse() {
                    assertFalse(tree.contains(null));
                }

                @Test
                @DisplayName("removeBranch elimina subárbol completo")
                void removeBranch_removesSubtree() {
                    tree.removeBranch(5);
                    assertFalse(tree.contains(5));
                    assertFalse(tree.contains(3));
                    assertFalse(tree.contains(7));
                    assertEquals(2, tree.size());
                }

                @Test
                @DisplayName("removeBranch con valor inexistente lanza excepción")
                void removeBranch_notFound_throwsException() {
                    assertThrows(BinarySearchTreeException.class, () -> tree.removeBranch(99));
                }

                @Test
                @DisplayName("removeBranch elimina nodo raíz")
                void removeBranch_removesRootNode() {
                    tree.removeBranch(10);
                    assertFalse(tree.contains(10));
                    assertFalse(tree.contains(5));
                    assertFalse(tree.contains(3));
                    assertFalse(tree.contains(7));
                    assertFalse(tree.contains(15));
                    assertEquals(0, tree.size());
                }

                @Test
                @DisplayName("removeValue con valor inexistente lanza excepción")
                void removeValue_notFound_throwsException() {
                    assertThrows(BinarySearchTreeException.class, () -> tree.removeValue(42));
                }

                @Test
                @DisplayName("removeValue elimina nodo hoja correctamente")
                void removeValue_leaf_removed() {
                    tree.removeValue(3);
                    assertFalse(tree.contains(3));
                    assertEquals(4, tree.size());
                }

                @Test
                @DisplayName("removeValue elimina nodo con un hijo")
                void removeValue_oneChild_removed() {
                    tree.insert(6);
                    tree.removeValue(7);
                    assertFalse(tree.contains(7));
                    assertTrue(tree.contains(6));
                }

                @Test
                @DisplayName("removeValue elimina nodo con dos hijos")
                void removeValue_twoChildren_removed() {
                    tree.removeValue(5);
                    assertFalse(tree.contains(5));
                    assertTrue(tree.contains(3));
                    assertTrue(tree.contains(7));
                    assertEquals(4, tree.size());
                }

                @Test
                @DisplayName("removeValue reemplaza correctamente con sucesor más profundo")
                void removeValue_withDeepSuccessor_replacesCorrectly() {
                    tree.insert(6);
                    tree.insert(8);
                    tree.insert(9);
                    tree.removeValue(7);
                    assertFalse(tree.contains(7));
                    assertTrue(tree.contains(6));
                    assertTrue(tree.contains(8));
                    assertTrue(tree.contains(9));
                }

                @Test
                @DisplayName("removeValue elimina nodo con solo hijo izquierdo")
                void removeValue_onlyLeftChild() {
                    tree.insert(6);        // hijo izquierdo
                    tree.insert(4);        // para asegurarnos de que 5 tenga solo hijo izquierdo
                    tree.removeValue(5);
                    assertFalse(tree.contains(5));
                    assertTrue(tree.contains(6)); // sigue presente
                }

                
        @Test
        @DisplayName("removeValue elimina nodo con solo hijo derecho")
        void removeValue_onlyRightChild() {
            tree.insert(6);        // hijo derecho
            tree.removeValue(5);   // 5 tiene solo hijo derecho (6)
            assertFalse(tree.contains(5));
            assertTrue(tree.contains(6));
        }

        @Test
        @DisplayName("removeBranch elimina nodo profundo (rama derecha)")
        void removeBranch_deepRightNode() {
            tree.insert(20);
            tree.insert(25); // rama más profunda
            tree.removeBranch(25);
            assertFalse(tree.contains(25));
        }

        @Nested
        @DisplayName("Después de eliminar un valor")
        class AfterRemoveValue {

        @BeforeEach
        void removeValue() {
            tree.removeValue(5);
        }

        @Test
        @DisplayName("El valor eliminado ya no está en el árbol")
        void valueIsRemoved() {
            assertFalse(tree.contains(5));
        }

        @Test
        @DisplayName("Tamaño del árbol decrece")
        void sizeDecreased() {
            assertEquals(4, tree.size());
        }
        }

        @Nested
        @DisplayName("Después de balancear el árbol")
        class AfterBalance {

            int depthBefore;

            @BeforeEach
            void balanceTree() {
                depthBefore = tree.depth();
                tree.balance();
            }

            @Test
            @DisplayName("La profundidad disminuye o se mantiene")
            void depthDecreases() {
                assertTrue(tree.depth() <= depthBefore);
            }

            @Test
            @DisplayName("inOrder() sigue siendo correcta tras balancear")
            void inOrderUnchanged() {
                assertEquals(List.of(3, 5, 7, 10, 15), tree.inOrder());
            }
        }
    }
    }
}
}