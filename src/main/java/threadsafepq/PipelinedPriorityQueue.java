package threadsafepq;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class PipelinedPriorityQueue<E> implements Serializable, BlockingQueue<E> {

    private static final int DEFAULT_CAPACITY_NUM_ELEMENTS = 11;
    private static final int DEFAULT_CAPACITY_NUM_LEVELS = 4;

    private BinaryArrayElement[] binaryArray;
    private TokenArrayElement[] tokenArray;
    private Comparator<? super E> comparator;

    public PipelinedPriorityQueue() {
        init(DEFAULT_CAPACITY_NUM_ELEMENTS, DEFAULT_CAPACITY_NUM_LEVELS, null);
    }

    public PipelinedPriorityQueue(Collection<? extends E> c) {
        if (c == null) throw new IllegalArgumentException("Input collection cannot be null");
        int capacity = c.size();
        int levels = BinaryTreeUtils.convertSizeToNumLevels(capacity);
        init(capacity, levels, null);
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
    }

    private void initInternalArrays() {
        initBinaryArray();
        initTokenArray();
    }

    private void initBinaryArray() {
        traverse(0);
    }

    /**
     * Recursive post-order traversal to initialise elements in the binary array
     *
     * @param i index of element to be initialised
     */
    private void traverse(int i) {
        int leftChildIndex = getLeftIndex(i);
        if (leftChildIndex <= binaryArray.length) {
            traverse(leftChildIndex);
        }

        int rightChildIndex = getRightIndex(i);
        if (rightChildIndex <= binaryArray.length) {
            traverse(rightChildIndex);
        }

        int capacity = getLeft(i).getCapacity() + getRight(i).getCapacity() + 2;
        BinaryArrayElement<E> element = new BinaryArrayElement<E>(false, null, capacity);
        binaryArray[i] = element;
    }

    private void initTokenArray() {
        for (int i = 0; i < tokenArray.length; i++) {
            TokenArrayElement<E> element = new TokenArrayElement<E>(TokenArrayElement.Operation.NO_OPERATION, null, -1);
            tokenArray[i] = element;
        }
    }

    private BinaryArrayElement getRoot() {
        return binaryArray[0];
    }

    private BinaryArrayElement getLeft(int index) {
        int leftIndex = index * 2 + 1;
        if (leftIndex >= binaryArray.length) {
            return null;
        }
        return binaryArray[index * 2 + 1];
    }

    private BinaryArrayElement getRight(int index) {
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
        return null;
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
        checkCapacity();
        //TODO:

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
        checkCapacity();
        return false;
    }

    public E take() throws InterruptedException {
        return null;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    public int remainingCapacity() {
        return 0;
    }

    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    public boolean add(E e) {
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {

    }

    public Iterator<E> iterator() {
        return null;
    }
}
