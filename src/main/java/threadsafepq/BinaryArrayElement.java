package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class BinaryArrayElement<E> {

    private boolean isActive;
    private E value;
    private int capacity;

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
