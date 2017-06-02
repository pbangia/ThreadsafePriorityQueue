package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by priyankitbangia on 02/06/2017.
 */
public class TokenElement<E> implements Serializable {

    private static final long serialVersionUID = 44L;

    private E value;
    private int position;
    private Comparator<? super E> comparator;
    private boolean isCompleted = false;

    public TokenElement(E value, int position, Comparator<? super E> comparator) {
        this.value = value;
        this.position = position;
        this.comparator = comparator;
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

        TokenElement<?> that = (TokenElement<?>) o;
        return new EqualsBuilder()
                .append(position, that.position)
                .append(value, that.value)
                .append(isCompleted, that.isCompleted)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(position)
                .append(isCompleted)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TokenElement{" +
                "isCompleted=" + isCompleted +
                ", value=" + value +
                ", position=" + position +
                '}';
    }

}
