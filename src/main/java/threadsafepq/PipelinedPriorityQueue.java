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
        this.binaryArray = new BinaryArrayElement[DEFAULT_CAPACITY_NUM_ELEMENTS];
        this.tokenArray = new TokenArrayElement[DEFAULT_CAPACITY_NUM_LEVELS];
    }

    public PipelinedPriorityQueue(Collection<? extends E> c) {
        if (c == null) throw new IllegalArgumentException("Input collection cannot be null");
        int size = c.size();
        int levels = BinaryTreeUtils.convertSizeToNumLevels(size);
        this.binaryArray = new BinaryArrayElement[size];
        this.tokenArray = new TokenArrayElement[levels];
    }

    public PipelinedPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be greater than 0");
        int levels = BinaryTreeUtils.convertSizeToNumLevels(initialCapacity);
        this.binaryArray = new BinaryArrayElement[initialCapacity];
        this.tokenArray = new TokenArrayElement[levels];
    }

    public PipelinedPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be greater than 0");
        if (comparator == null) throw new IllegalArgumentException("Input comparator cannot be null");
        int levels = BinaryTreeUtils.convertSizeToNumLevels(initialCapacity);
        this.binaryArray = new BinaryArrayElement[initialCapacity];
        this.tokenArray = new TokenArrayElement[levels];
        this.comparator = comparator;
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
