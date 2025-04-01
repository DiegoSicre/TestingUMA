package binarySearchTree;

public interface BinarySearchTreeStructure<T> {

    // Basic operations

    void insert (T value);

    boolean isLeaf();

    boolean contains(T value);

    T minimum();

    T maximum(); 

    void removeBranch(T value); 

    int size();

    int depth(); 

    // Complex operations

    // (Estas operaciones se incluirán más adelante para ser realizadas en la segunda practica

}