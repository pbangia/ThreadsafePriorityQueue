package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class BinaryArrayElement<E> implements Serializable {

    private static final long serialVersionUID = 43L;

    private boolean isActive;
    private E value;
    private int capacity;
    private BinaryArrayElement<E> leftChild;
    private BinaryArrayElement<E> rightChild;
    private Comparator<? super E> comparator;
    private final ReentrantLock reentrantLock;

    public BinaryArrayElement(boolean isActive, E value, int capacity, BinaryArrayElement<E> leftChild,
                              BinaryArrayElement<E> rightChild, Comparator<? super E> comparator,
                              ReentrantLock reentrantLock) {
        this.isActive = isActive;
        this.value = value;
        this.capacity = capacity;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.comparator = comparator;
        this.reentrantLock = reentrantLock;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isGreaterThan(E o) {
        int result;
        if (comparator != null) {
            result = comparator.compare(o, value);
        } else {
            result = ((Comparable<? super E>) o).compareTo(this.value);
        }

        return (result > 0);
    }

    public void decrementCapacity() {
        this.capacity--;
    }

    public void incrementCapacity() {
        this.capacity++;
    }

    public void lock() {
        reentrantLock.lock();
        if (leftChild != null) {
            leftChild.lock();
        }
        if (rightChild != null) {
            rightChild.lock();
        }
    }

    public void unlock() {
        reentrantLock.unlock();
        if (leftChild != null) {
            leftChild.unlock();
        }
        if (rightChild != null) {
            rightChild.unlock();
        }
    }

    public void unlockRightChild() {
        if (rightChild != null) {
            rightChild.unlock();
        }
    }

    public void unlockLeftChild() {
        if (leftChild != null) {
            leftChild.unlock();
        }
    }

    public void unlockNode() {
        reentrantLock.unlock();
    }

    public void unlockButKeepLeft() {
        unlockNode();
        unlockRightChild();
    }

    public void unlockButKeepRight() {
        unlockNode();
        unlockLeftChild();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Comparator<? super E> getComparator() {
        return comparator;
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
