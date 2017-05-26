package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class TokenArrayElement<E> {

    private Operation operation;
    private E value;
    private int position;

    public TokenArrayElement(Operation operation, E value, int position) {
        this.operation = operation;
        this.value = value;
        this.position = position;
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
