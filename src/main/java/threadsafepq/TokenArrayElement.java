package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A single instance of TokenArrayElement<E> represents a single level
 * of the pipelined heap used in the queue.
 */
public class TokenArrayElement<E> implements Serializable {

    /**
     * Used to uniquely serialize an instance of this class.
     */
    private static final long serialVersionUID = 44L;
    /**
     * Represents the generic type value to be stored inside the node.
     */
    private E value;
    /**
     * Represents a position of a node in the binary tree.
     */
    private int position;
    /**
     * Represents the user-defined comparator passed in the construction of the queue.
     * This is used to determine the relative priorities of different nodes.
     */
    private Comparator<? super E> comparator;
    /**
     * Represents the Lock associated with this TokenArrayElement used to control concurrent read/write access.
     */
    private final TokenLock tokenLock;

    /**
     * Constructs a single instance of TokenArrayElement<E>
     *
     * @param value      nullable value representing the node
     * @param position   position of a node in the binary tree
     * @param comparator comparator used to determine the relative priorities of different nodes
     * @param tokenLock  TokenLock used to control concurrent access with this instance
     */
    public TokenArrayElement(E value,
                             int position,
                             Comparator<? super E> comparator,
                             TokenLock tokenLock) {
        this.value = value;
        this.position = position;
        this.comparator = comparator;
        this.tokenLock = tokenLock;
    }

    /**
     * Locks this TokenArrayElement.
     */
    public void lock() {
        tokenLock.lock();
    }

    /**
     * Unlocks this TokenArrayElement.
     */
    public void unlock() {
        tokenLock.unlock();
    }

    /**
     * Returns the TokenLock associated with this TokenArrayElement
     * @return TokenLock associated with this TokenArrayElement
     */
    public TokenLock getLock() {
        return tokenLock;
    }

    /**
     * Returns the value associated with this TokenArrayElement
     * @return the value associated with this TokenArrayElement
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value associated with this TokenArrayElement
     * @param value the new value to be associated with this TokenArrayElement
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Returns the position associated with this TokenArrayElement
     * @return position associated with this TokenArrayElement
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position associated with this TokenArrayElement
     * @param position new position to be associated with this TokenArrayElement
     */
    public void setPosition(int position) {
        this.position = position;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TokenArrayElement<?> that = (TokenArrayElement<?>) o;
        return new EqualsBuilder()
                .append(position, that.position)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(position)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TokenArrayElement{" +
                ", value=" + value +
                ", position=" + position +
                '}';
    }

    protected static class TokenLock extends ReentrantLock {

        public TokenLock(boolean fair) {
            super(fair);
        }

        public Thread getOwner() {
            return super.getOwner();
        }

    }

}
