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
    private Comparator<? super E> comparator;
    private final ReentrantLock reentrantLock;

    public BinaryArrayElement(boolean isActive, E value, int capacity, Comparator<? super E> comparator, ReentrantLock reentrantLock) {
        this.isActive = isActive;
        this.value = value;
        this.capacity = capacity;
        this.comparator = comparator;
        this.reentrantLock = reentrantLock;
    }

    public boolean isActive() {
        return isActive;
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

    public void decrementCapacity() {
        this.capacity--;
    }

    public void incrementCapacity() {
        this.capacity++;
    }

    public void lock() {
        reentrantLock.lock();
    }

    public void unlock() {
        reentrantLock.unlock();
    }
}
