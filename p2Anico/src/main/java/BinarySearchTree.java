import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> implements BinarySearchTreeStructure<T> {

    // Clase interna que representa un nodo del árbol binario
    private static class Node<T> {
        T value;
        Node<T> left, right;

        Node(T value) {
            this.value = value;
            left = right = null;
        }
    }

    private Node<T> root; // Nodo raíz del árbol
    private int size;     // Número de elementos en el árbol

    @Override
    public void insert(T value) {
        if (value == null) throw new IllegalArgumentException("No se puede insertar un valor nulo.");
        root = insertRec(root, value);
        size++; // Aumentamos el tamaño sólo si la inserción es válida
    }

    // Inserta un valor recursivamente en el árbol respetando la propiedad de BST
    private Node<T> insertRec(Node<T> node, T value) {
        if (node == null) return new Node<>(value); // Caso base: insertar nuevo nodo
        int cmp = value.compareTo(node.value);
        if (cmp < 0)
            node.left = insertRec(node.left, value); // Insertar en el subárbol izquierdo
        else if (cmp > 0)
            node.right = insertRec(node.right, value); // Insertar en el subárbol derecho
        else
            size--; // Si ya existe, no se incrementa el tamaño
        return node;
    }

    @Override
    public boolean isLeaf() {
        if (root == null) throw new BinarySearchTreeException("El árbol está vacío.");
        return root.left == null && root.right == null; // La raíz no tiene hijos
    }

    @Override
    public boolean contains(T value) {
        if (value == null) return false;
        return containsRec(root, value);
    }

    // Busca recursivamente un valor en el árbol
    private boolean containsRec(Node<T> node, T value) {
        if (node == null) return false;
        int cmp = value.compareTo(node.value);
        if (cmp < 0) return containsRec(node.left, value);
        if (cmp > 0) return containsRec(node.right, value);
        return true; // Valor encontrado
    }

    @Override
    public T minimum() {
        if (root == null) throw new BinarySearchTreeException("El árbol está vacío.");
        return minNode(root).value;
    }

    // Busca el nodo con el valor mínimo recorriendo hacia la izquierda
    private Node<T> minNode(Node<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public T maximum() {
        if (root == null) throw new BinarySearchTreeException("El árbol está vacío.");
        return maxNode(root).value;
    }

    // Busca el nodo con el valor máximo recorriendo hacia la derecha
    private Node<T> maxNode(Node<T> node) {
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public void removeBranch(T value) {
        if (!contains(value)) throw new BinarySearchTreeException("Element not found.");
        root = removeBranchRec(root, value);
        int after = countNodes(root);
        size = after; // Ajustamos correctamente
    }

    private int countNodes(Node<T> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    // Elimina el nodo con ese valor y toda su subrama
    private Node<T> removeBranchRec(Node<T> node, T value) {
        if (node == null) return null;
        int cmp = value.compareTo(node.value);
        if (cmp < 0)
            node.left = removeBranchRec(node.left, value);
        else if (cmp > 0)
            node.right = removeBranchRec(node.right, value);
        else
            return null; // Se elimina toda la subrama
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        return depthRec(root);
    }

    // Calcula la profundidad máxima del árbol recursivamente
    private int depthRec(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(depthRec(node.left), depthRec(node.right));
    }

    @Override
    public void removeValue(T value) {
        if (!contains(value)) throw new BinarySearchTreeException("El elemento no está presente.");
        root = removeValueRec(root, value);
        size--;
    }

    // Elimina un nodo específico sin eliminar toda la rama
    private Node<T> removeValueRec(Node<T> node, T value) {
        if (node == null) return null;
        int cmp = value.compareTo(node.value);
        if (cmp < 0)
            node.left = removeValueRec(node.left, value);
        else if (cmp > 0)
            node.right = removeValueRec(node.right, value);
        else {
            // Nodo con un solo hijo o sin hijos
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Nodo con dos hijos: reemplazamos con el sucesor
            Node<T> successor = minNode(node.right);
            node.value = successor.value;
            node.right = removeValueRec(node.right, successor.value);
        }
        return node;
    }

    @Override
    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    // Recorre el árbol en orden (izquierda, raíz, derecha)
    private void inOrderRec(Node<T> node, List<T> list) {
        if (node != null) {
            inOrderRec(node.left, list);
            list.add(node.value);
            inOrderRec(node.right, list);
        }
    }

    @Override
    public void balance() {
        List<T> sorted = inOrder(); // Lista ordenada de los valores
        root = buildBalancedTree(sorted, 0, sorted.size() - 1);
    }

    // Construye un árbol equilibrado a partir de una lista ordenada
    private Node<T> buildBalancedTree(List<T> sorted, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        Node<T> node = new Node<>(sorted.get(mid));
        node.left = buildBalancedTree(sorted, low, mid - 1);
        node.right = buildBalancedTree(sorted, mid + 1, high);
        return node;
    }
}
