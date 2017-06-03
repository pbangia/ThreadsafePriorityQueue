package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class TokenArrayElement<E> implements Serializable {

    private static final long serialVersionUID = 44L;

    private Operation operation;
    private E value;
    private int position;
    private Comparator<? super E> comparator;

    public TokenArrayElement(Operation operation, E value, int position, Comparator<? super E> comparator) {
        this.operation = operation;
        this.value = value;
        this.position = position;
        this.comparator = comparator;
    }

    protected enum Operation {
        ENQUEUE,
        DEQUEUE,
        ENQUEUE_DEQUEUE,
        NO_OPERATION
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

        TokenArrayElement<?> that = (TokenArrayElement<?>) o;
        return new EqualsBuilder()
                .append(position, that.position)
                .append(operation, that.operation)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(operation)
                .append(value)
                .append(position)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TokenArrayElement{" +
                "operation=" + operation +
                ", value=" + value +
                ", position=" + position +
                '}';
    }

}