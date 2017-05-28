package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class PipelinedPriorityQueue<E> implements Serializable, BlockingQueue<E> {

    private static final long serialVersionUID = 42L;

    private static final int DEFAULT_CAPACITY_NUM_ELEMENTS = 11;
    private static final int DEFAULT_CAPACITY_NUM_LEVELS = 4;

    private BinaryArrayElement<E>[] binaryArray;
    private TokenArrayElement<E>[] tokenArray;
    private Comparator<? super E> comparator;
    private int size = 0;

    public PipelinedPriorityQueue() {
        init(DEFAULT_CAPACITY_NUM_ELEMENTS, DEFAULT_CAPACITY_NUM_LEVELS, null);
    }

    public PipelinedPriorityQueue(Collection<? extends E> c) {
        if (c == null) throw new IllegalArgumentException("Input collection cannot be null");
        int capacity = c.size();
        int levels = BinaryTreeUtils.convertSizeToNumLevels(capacity);
        init(capacity, levels, null);
        addAll(c);
    }

    public PipelinedPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be greater than 0");
        int levels = BinaryTreeUtils.convertSizeToNumLevels(initialCapacity);
        init(initialCapacity, levels, null);
    }

    public PipelinedPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be greater than 0");
        if (comparator == null) throw new IllegalArgumentException("Input comparator cannot be null");
        int levels = BinaryTreeUtils.convertSizeToNumLevels(initialCapacity);
        init(initialCapacity, levels, comparator);
    }

    private void init(int capacity, int levels, Comparator<? super E> comparator) {
        this.binaryArray = new BinaryArrayElement[capacity];
        this.tokenArray = new TokenArrayElement[levels];
        this.comparator = comparator;
        initInternalArrays();
        for (int i = 0; i < capacity; i++) {
            System.out.println(binaryArray[i].getCapacity());
        }
    }

    private void initInternalArrays() {
        initBinaryArray();
        initTokenArray();
    }

    private void initBinaryArray() {
        initBinaryArrayElement(0);
    }

    /**
     * Recursive post-order traversal to initialise elements in the binary array
     *
     * @param i index of element to be initialised
     */
    private void initBinaryArrayElement(int i) {
        if (i >= binaryArray.length) return;
        initBinaryArrayElement(getLeftIndex(i));
        initBinaryArrayElement(getRightIndex(i));

        int capacity = 1;

        BinaryArrayElement<E> leftChild = getLeft(i);
        if (leftChild != null) {
            capacity += leftChild.getCapacity();
        }

        BinaryArrayElement<E> rightChild = getRight(i);
        if (rightChild != null) {
            capacity += rightChild.getCapacity();
        }

        BinaryArrayElement<E> element = new BinaryArrayElement<E>(false, null, capacity);
        binaryArray[i] = element;
    }

    private void initTokenArray() {
        for (int i = 0; i < tokenArray.length; i++) {
            TokenArrayElement<E> element = new TokenArrayElement<E>(TokenArrayElement.Operation.NO_OPERATION, null, -1);
            tokenArray[i] = element;
        }
    }

    private BinaryArrayElement<E> getRoot() {
        return binaryArray[0];
    }

    private BinaryArrayElement<E> getLeft(int index) {
        int leftIndex = index * 2 + 1;
        if (leftIndex >= binaryArray.length) {
            return null;
        }
        return binaryArray[index * 2 + 1];
    }

    private BinaryArrayElement<E> getRight(int index) {
        int rightIndex = index * 2 + 2;
        if (rightIndex >= binaryArray.length) {
            return null;
        }
        return binaryArray[index * 2 + 2];
    }

    private int getLeftIndex(int index) {
        return index * 2 + 1;
    }

    private int getRightIndex(int index) {
        return index * 2 + 2;
    }

    public boolean offer(E e) {
        return false;
    }

    public E remove() {
        if (size == 0) {
            // TODO block thread
            return null;
        }

        E value = binaryArray[0].getValue();
        binaryArray[0].setActive(false);
        binaryArray[0].incrementCapacity();
        tokenArray[0].setPosition(0);

        int level = 0;
        boolean done = false;
        while (level < tokenArray.length && !done) {
            done = localDequeue(level);
            level++;
            if (level == tokenArray.length) {
                // TODO double the arrays as no space
                return null;
            }
        }

        size--;
        return value;
    }

    private boolean localDequeue(int i) {
        int current = tokenArray[i].getPosition();
        if ((getLeft(current) == null || !getLeft(current).isActive())
                && (getRight(current) == null || !getRight(current).isActive())) {
            return true;
        }

        BinaryArrayElement<E> leftChild = getLeft(current);
        BinaryArrayElement<E> rightChild = getRight(current);
        BinaryArrayElement<E> greatestChild;
        int greatestChildPosition;

        if (leftChild == null) {
            greatestChild = rightChild;
            greatestChildPosition = getRightIndex(current);
        } else if (rightChild == null || leftChild.isGreaterThan(rightChild.getValue())) {
            greatestChild = leftChild;
            greatestChildPosition = getLeftIndex(current);
        } else {
            greatestChild = rightChild;
            greatestChildPosition = getRightIndex(current);
        }

        binaryArray[current].setActive(true);
        binaryArray[current].setValue(greatestChild.getValue());
        greatestChild.setActive(false);
        greatestChild.incrementCapacity();
        tokenArray[i + 1].setPosition(greatestChildPosition);
        return false;
    }

    public E poll() {
        return null;
    }

    public E element() {
        return null;
    }

    public E peek() {
        return null;
    }

    public void put(E e) throws InterruptedException {
        tokenArray[0].setValue(e);
        tokenArray[0].setPosition(0);

        int level = 0;
        boolean success = false;
        while (level < tokenArray.length) {
            boolean result = localEnqueue(level);
            if (result) {
                success = true;
                break;
            } else {
                level++;
            }
        }

        if (!success) {
            resize();
            put(e);
        }

        size++;
    }

    private boolean localEnqueue(int i) {
        int position = tokenArray[i].getPosition();
        E value = tokenArray[i].getValue();
        if (!binaryArray[position].isActive()) {
            binaryArray[position].setValue(value);
            binaryArray[position].setActive(true);
            binaryArray[position].decrementCapacity();
            return true;
        } else if (tokenArray[i].isGreaterThan(binaryArray[position].getValue())) {
            E temp = tokenArray[i].getValue();
            tokenArray[i].setValue(binaryArray[position].getValue());
            binaryArray[position].setValue(temp);
        }

        tokenArray[i + 1].setValue(tokenArray[i].getValue());
        tokenArray[i].setValue(null);
        if (getLeft(position).getCapacity() > 0) {
            tokenArray[i + 1].setPosition(getLeftIndex(position));
        } else {
            tokenArray[i + 1].setPosition(getRightIndex(position));
        }
        return false;
    }

    private void checkCapacity() {
        if (getRoot().getCapacity()<1){
            int newSize = binaryArray.length * 2;
            BinaryArrayElement[] temp = new BinaryArrayElement[newSize];

            int elementPos = 1;
            for (BinaryArrayElement e: binaryArray){
                e.setCapacity(e.getCapacity()*2); //needs fixing
                temp[elementPos] = e;
                elementPos++;
            }

            binaryArray = temp;
        }
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public E take() throws InterruptedException {
        return null;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    public int remainingCapacity() {
        return size == 0
                ? getRoot().getCapacity() + 1
                : getRoot().getCapacity();
    }

    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size==0);
    }

    public boolean contains(Object o) {

        for (BinaryArrayElement e: binaryArray){
            if (e.getValue() == null) continue;
            if (e.getValue().equals(o)) return true;
        }
        return false;
    }

    public synchronized Object[] toArray() {
        Object[] result = new Object[size];
        int taken = 0;
        int runner = 0;
        while (taken < size && runner < binaryArray.length) {
            if (binaryArray[runner].getValue() != null) {
                result[taken] = binaryArray[runner].getValue();
                taken++;
            }
            runner++;
        }

        if (taken != size) {
            // TODO something went wrong
        }

        return result;
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    public boolean add(E e) {
        boolean added = false;
        while (!added) {
            try {
                put(e);
                added = true;
            } catch (InterruptedException ex) {}
        }
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        if (c.size()>size) return false;

        HashSet set1 = new HashSet(c);
        int count = 0;
        for (BinaryArrayElement e: binaryArray){
            if (e.getValue()==null) continue;
            if (set1.contains(e.getValue())) count++;
        }
        return set1.size()==count;
    }

    public boolean addAll(Collection<? extends E> c) {
        for (E e:c){
            add(e);
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        initBinaryArray();
        initTokenArray();
    }

    public Iterator<E> iterator() {

        return new Iterator<E>() {
            public boolean hasNext() {
                return peek()!=null;
            }

            public E next() {
                if (peek()==null) throw new NoSuchElementException();
                boolean taken = false;
                E e = null;
                while (!taken){
                    try {
                        e = take();
                    } catch (InterruptedException ex) {}
                }
                return e;
            }

            public void remove() {}
        };
    }

    /**
     * Increases the capacity of the array.
     */
    private void resize() {
        int oldCapacity = binaryArray.length;
        // Double size if small; else grow by 50%
        int newCapacity = ((oldCapacity < 64) ?
                ((oldCapacity + 1) * 2) :
                ((oldCapacity / 2) * 3));
        if (newCapacity < 0) // overflow
            newCapacity = Integer.MAX_VALUE;

        BinaryArrayElement<E>[] oldBinaryArray = binaryArray;
        binaryArray = new BinaryArrayElement[newCapacity];
        initBinaryArray();
        for (int i = 0; i < oldCapacity; i++) {
            binaryArray[i] = oldBinaryArray[i];
        }

        tokenArray = new TokenArrayElement[BinaryTreeUtils.convertSizeToNumLevels(newCapacity)];
        initTokenArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PipelinedPriorityQueue<?> that = (PipelinedPriorityQueue<?>) o;

        return new EqualsBuilder()
                .append(binaryArray, that.binaryArray)
                .append(tokenArray, that.tokenArray)
                .append(comparator, that.comparator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(binaryArray)
                .append(tokenArray)
                .append(comparator)
                .toHashCode();
    }
}
