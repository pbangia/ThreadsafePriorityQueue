package threadsafepq;

/**
 * Set of utility functions associated with binary trees.
 */
public class BinaryTreeUtils {

    /**
     * Returns the number of levels in a binary tree given its size
     *
     * @param size size of binary tree
     * @return number of levels in binary tree
     */
    public static int convertSizeToNumLevels(int size) {
        return (int) (Math.ceil(Math.log(size + 1) / Math.log(2)));
    }

    /**
     * Returns the size of a complete and full binary tree given the number of levels it has
     * @param levels number of levels in binary tree
     * @return size of complete and full binary tree
     */
    public static int convertNumLevelsToSize(int levels) {
        return (int) (Math.pow(2, levels) - 1);
    }

    /**
     * Returns the level of a node given its index in an array
     * @param index index of a node in a zero-indexed array
     * @return level of a node
     */
    public static int findLevel(int index) {
        if (index <= 0) return 1;
        return (int) (Math.log(index) / Math.log(2) + 1);
    }

}
