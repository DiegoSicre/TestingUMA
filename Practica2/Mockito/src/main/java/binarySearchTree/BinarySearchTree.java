package binarySearchTree;
import java.util.Comparator;

public class BinarySearchTree<T> implements BinarySearchTreeStructure<T> {
    private Comparator<T> comparator;
    private T value;
    private BinarySearchTree<T> left;
    private BinarySearchTree<T> right;

    
    public BinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
        this.value = null;
        this.left = null;
        this.right = null;
    }
    
  

    @Override
    public void insert(T value) {
        if (this.value == null) {
            this.value = value;
            return;
        }
    
        int cmp = comparator.compare(value, this.value);
        
        if (cmp < 0) {
            if (left == null) {
                left = new BinarySearchTree<>(comparator);
            }
            left.insert(value);
        } else if (cmp > 0) {
            if (right == null) {
                right = new BinarySearchTree<>(comparator);
            }
            right.insert(value);
        }
        // Si cmp == 0 no insertamos duplicados
    }

    @Override
    public boolean isLeaf() {
        return (this.left == null && this.right == null);
        
    }

    @Override
    public boolean contains(T value) {
        int cmp = comparator.compare(value, this.value);
        if(cmp == 0){
            return true;
        }
        if(cmp < 0){
            if(left == null) return false;
            return left.contains(value);
        }else{
            if(right == null) return false;
            return right.contains(value);
        }


    }

    @Override
    public T minimum() {
        if(isLeaf()) return value;
        if(left == null) return value;
        return left.minimum();
    }

    @Override
    public T maximum() {
        if(isLeaf()) return value;
        if(right == null) return value;
        return right.maximum();
    }
    @Override
    public void removeBranch(T value) {
        if (this.value == null) {
            return; // Árbol vacío
        }
    
        // Intentamos eliminar en el hijo izquierdo
        if (left != null) {
            int cmpLeft = comparator.compare(value, left.value);
            if (cmpLeft == 0) {
                left = null;
                return;
            } else if (cmpLeft < 0) {
                left.removeBranch(value);
                return;
            }
        }
    
        // Intentamos eliminar en el hijo derecho
        if (right != null) {
            int cmpRight = comparator.compare(value, right.value);
            if (cmpRight == 0) {
                right = null;
                return;
            } else if (cmpRight > 0) {
                right.removeBranch(value);
            }
        }
    }
    

    @Override
    public int size() {
        if (this.value == null) {
            return 0; // Nodo vacío
        }
    
        int leftSize = (left != null) ? left.size() : 0;
        int rightSize = (right != null) ? right.size() : 0;
    
        return 1 + leftSize + rightSize;
    }
    
    @Override
    public int depth() {
        if (this.value == null) {
            return 0; // Árbol vacío
        }
    
        int leftDepth = (left != null) ? left.depth() : 0;
        int rightDepth = (right != null) ? right.depth() : 0;
    
        return 1 + Math.max(leftDepth, rightDepth);
    }
}