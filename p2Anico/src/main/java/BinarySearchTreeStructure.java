import java.util.List;

/**
 * A binary search tree is a binary tree in which each node has a value and two subtrees, and for each node
 * all elements in the left subtree are less than the node value, and all elements in the right
 * subtree are greater than the node value.
 *
 * @param <T> the type of elements held in this binary tree
 */
public interface BinarySearchTreeStructure<T> {
    // Basic operations

    /**
     * Inserts the specified element in order in the binary search tree.
     *
     * @param value the value to be inserted
     */
    void insert (T value);

    /**
     * Returns {@code true} if the binary search tree has no children.
     *
     * @return {@code true} if this binary tree has no children
     * @throws BinaryTreeException if the binary tree is empty
     */
    boolean isLeaf();

    /**
     * Returns {@code true} if this binary tree contains the specified element.
     * <p>
     * More formally, returns {@code true} if and only if this binary tree contains at least one
     * element {@code e} such that {@code Objects.equals(e, value)}.
     *
     * @param value whose presence in this binary tree is to be tested
     * @return {@code true} if this binary tree contains the specified element
     */
    boolean contains(T value);

    /**
     * Returns minimum value of the binary search tree.
     *
     * @return the minimum value of the binary search tree
     * @throws BinaryTreeException if the binary tree is empty
     */
    T minimum();

    /**
     * Returns maximum value of the binary search tree.
     *
     * @return the maximum value of the binary search tree
     * @throws BinaryTreeException if the binary tree is empty
     */
    T maximum();

    /**
     * Remove the branch with the specified value from the binary search tree.
     * @throws BinaryTreeException if the element is not present in the binary search tree
     */
    void removeBranch(T value);

    /**
     * Returns the number of elements in this binary tree.
     *
     * @return the number of elements in this binary tree
     */
    int size();

    /**
     * Returns the maximum depth of this binary tree. The depth of a binary tree is the number of nodes along the longest path
     *
     * @return the maximum depth of this binary tree
     */
    int depth();

    // Complex operations

    /**
     * Removes the first occurrence of the specified element from this binary search tree, if it is present.
     *
     * @param value to be removed from this binary tree, if present
     * @throws BinaryTreeException if the element is not present in the binary tree
     */
    void removeValue(T value);

    /**
     * Returns a List of all the values of the tree in order.
     *
     * @return a List of all the values of the tree in order
     */
    List<T> inOrder();

    /**
     * Balance the binary search tree. Making the depth of the
     * left and right subtrees of every node differ by at most one.
     */
    void balance();
}