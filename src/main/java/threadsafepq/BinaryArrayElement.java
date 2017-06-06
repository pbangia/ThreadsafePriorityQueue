package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A single instance of BinaryArrayElement<E> represents
 * a single node in the heap (binary tree) of the pipelined heap used for the queue.
 */
public class BinaryArrayElement<E> implements Serializable {

    /**
     * Used to uniquely serialize an instance of this class.
     */
    private static final long serialVersionUID = 43L;

    /**
     * True if this node is a non-null entry in the heap, false otherwise.
     */
    private boolean isActive;
    /**
     * Represents the generic type value to be stored inside the node.
     */
    private E value;
    /**
     * Represents the number of inactive nodes sub-rooted at this node.
     */
    private int capacity;
    /**
     * Represents the user-defined comparator passed in the construction of the queue.
     * This is used to determine the relative priorities of different nodes.
     */
    private Comparator<? super E> comparator;

    /**
     * Constructs a single instance of BinaryArrayElement<E>
     *
     * @param isActive   true if this node is active on construction
     * @param value      nullable value representing the node
     * @param capacity   number of inactive nodes sub-rooted at this node
     * @param comparator comparator used to determine the relative priorities of different nodes
     */
    public BinaryArrayElement(boolean isActive, E value, int capacity, Comparator<? super E> comparator) {
        this.isActive = isActive;
        this.value = value;
        this.capacity = capacity;
        this.comparator = comparator;
    }

    /**
     * Returns true if this BinaryArrayElement is active
     * @return true if this BinaryArrayElement is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Returns true if the value in this BinaryArrayElement is greater than the argument value.
     * This implementation will use natural ordering to determine the relativity in priorities between
     * the two values if no comparator is provided.
     * @param o The other value to compare this BinaryArrayElement's value with
     * @return true if the value in this BinaryArrayElement is greater than the argument value
     */
    public boolean isGreaterThan(E o) {
        int result;
        if (comparator != null) {
            result = comparator.compare(o, value);
        } else {
            result = ((Comparable<? super E>) o).compareTo(this.value);
        }

        return (result > 0);
    }

    /**
     * Decrements the capacity of this BinaryArrayElement.
     */
    public void decrementCapacity() {
        this.capacity--;
    }

    /**
     * Increments the capacity of this BinaryArrayElement.
     */
    public void incrementCapacity() {
        this.capacity++;
    }

    /**
     * Sets the active field to the argument provided.
     * @param active true if this instance's active field needs to be set to true, false otherwise.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Returns the value associated with this BinaryArrayElement
     * @return the value associated with this BinaryArrayElement
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value associated with this BinaryArrayElement
     * @param value the new value to be associated with this BinaryArrayElement
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Returns the capacity of this BinaryArrayElement
     * @return the capacity of this BinaryArrayElement
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of this BinaryArrayElement
     * @param capacity the new capacity of this BinaryArrayElement
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BinaryArrayElement<?> that = (BinaryArrayElement<?>) o;
        return new EqualsBuilder()
                .append(isActive, that.isActive)
                .append(capacity, that.capacity)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(isActive)
                .append(value)
                .append(capacity)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BinaryArrayElement{" +
                "isActive=" + isActive +
                ", value=" + value +
                ", capacity=" + capacity +
                '}';
    }
}
