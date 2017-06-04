package threadsafepq;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PipelinedPriorityQueue<E> implements Serializable, BlockingQueue<E> {

    private static final long serialVersionUID = 42L;

    private static final int DEFAULT_CAPACITY_NUM_ELEMENTS = 11;
    private static final int DEFAULT_CAPACITY_NUM_LEVELS = 4;

    private TokenArrayElement<E>[] tokenArray;
    private BinaryArrayElement<E>[] binaryArray;
    private Comparator<? super E> comparator;
    private AtomicInteger size;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private int treeHeight;

    /**
     * Creates a PriorityBlockingQueue with the default initial capacity (11)
     * that orders its elements according to their natural ordering.
     */
    public PipelinedPriorityQueue() {
        init(DEFAULT_CAPACITY_NUM_ELEMENTS, DEFAULT_CAPACITY_NUM_LEVELS, null);
    }

    /**
     * Creates a PriorityBlockingQueue containing the elements in the specified collection.
     *
     * @param c the collection whose elements are to be placed into this priority queue
     */
    public PipelinedPriorityQueue(Collection<? extends E> c) {
        if (c == null) throw new IllegalArgumentException("Input collection cannot be null");
        int capacity = c.size();
        int levels = BinaryTreeUtils.convertSizeToNumLevels(capacity);
        init(capacity, levels, null);
        addAll(c);
    }

    /**
     * Creates a PriorityBlockingQueue with the specified initial capacity that orders
     * its elements according to their natural ordering.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @throws IllegalArgumentException if initialCapacity is less than 1
     */
    public PipelinedPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be greater than 0");
        int levels = BinaryTreeUtils.convertSizeToNumLevels(initialCapacity);
        init(initialCapacity, levels, null);
    }

    /**
     * Creates a PriorityBlockingQueue with the specified initial capacity that orders
     * its elements according to the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator      the comparator that will be used to order this priority queue. If null, the natural ordering of the elements will be used.
     */
    public PipelinedPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be greater than 0");
        int levels = BinaryTreeUtils.convertSizeToNumLevels(initialCapacity);
        init(initialCapacity, levels, comparator);
    }

    private void init(int capacity, int levels, Comparator<? super E> comparator) {
        this.binaryArray = new BinaryArrayElement[capacity];
        this.comparator = comparator;
        this.size = new AtomicInteger(0);
        this.tokenArray = new TokenArrayElement[levels];
        this.treeHeight = BinaryTreeUtils.convertSizeToNumLevels(capacity);
        initInternalArrays();
    }

    private void initInternalArrays() {
        initBinaryArray();
        initTokenArray();
    }

    private void initBinaryArray() {
        initBinaryArrayElement(0);
    }

    /**
     * Recursive postorder traversal to initialise elements in the binary array
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

        BinaryArrayElement<E> element = new BinaryArrayElement<E>(false, null, capacity, comparator);
        binaryArray[i] = element;
    }

    private void initTokenArray() {
        for (int i = 0; i < tokenArray.length; i++) {
            TokenArrayElement<E> element = new TokenArrayElement<E>(
                    TokenArrayElement.Operation.NO_OPERATION, null,
                    1, comparator, new ReentrantLock(true));
            tokenArray[i] = element;
        }
    }


    /**
     * Inserts the specified element into this priority queue.
     * As the queue is unbounded, this method will never return false.
     *
     * @param e the element to add
     * @return true
     * @throws ClassCastException   if the specified element cannot be compared with elements currently in the priority queue according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * Inserts the specified element into this priority queue.
     * As the queue is unbounded, this method will never return false.
     *
     * @param e the element to add
     * @return true
     * @throws ClassCastException   if the specified element cannot be compared with elements currently in the priority queue according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException("Specified element is null");
        tokenArray[0].lock();
        if (tokenArray.length > 1) tokenArray[1].lock();
        tokenArray[0].setValue(e);
        tokenArray[0].setPosition(0);

        if (binaryArray[0].getCapacity() < 1) {
            resize();
        }

        int level=0;
        while (level < tokenArray.length) {
            boolean result = localEnqueue(level);
            if (result) {
                size.getAndIncrement();
                tokenArray[level].unlock();
                if (level + 1 < tokenArray.length) tokenArray[level + 1].unlock();
                break;
            }
            tokenArray[level].unlock();
            level++;
            if (level + 1 < tokenArray.length) tokenArray[level + 1].lock();
        }

        return true;
    }

    /**
     * Inserts the specified element into this priority queue. As the queue is unbounded, this method will never block or return false.
     *
     * @param e       the element to add
     * @param timeout This parameter is ignored as the method never blocks
     * @param unit    This parameter is ignored as the method never blocks
     * @return true
     * @throws InterruptedException
     */
    public boolean offer(E e, long timeout, TimeUnit unit) {
        return offer(e);
    }

    /**
     * Inserts the specified element into this priority queue. As the queue is unbounded, this method will never block.
     *
     * @param e the element to add
     */
    public void put(E e) {
        offer(e);
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    public E peek() {
        return binaryArray[0].getValue();
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     * This method differs from peek only in that it throws an exception if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E element() {
        if (size.get() == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return peek();
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    public E poll() {
        tokenArray[0].lock();
        if (tokenArray.length > 1) tokenArray[1].lock();
        E value = binaryArray[0].getValue();
        binaryArray[0].setActive(false);
        binaryArray[0].incrementCapacity();
        tokenArray[0].setPosition(0);

        int level = 0;
        while (level < tokenArray.length) {
            boolean result = localDequeue(level);
            if (result) {
                size.decrementAndGet();
                tokenArray[level].unlock();
                if (level + 1 < tokenArray.length) tokenArray[level + 1].unlock();
                break;
            }
            tokenArray[level].unlock();
            level++;
            if (level + 1 < tokenArray.length) tokenArray[level + 1].lock();
        }
        return value;
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    public E take() throws InterruptedException {
        while (size.get() == 0) {
            notEmpty.await();
            // TODO update other methods to cause the signal for this await()
        }

        E head = poll();
        assert head != null;
        return head;
    }

    /**
     * Retrieves and removes the head of this queue,
     * waiting up to the specified wait time if necessary for an element to become available.
     *
     * @param timeout how long to wait before giving up, in units of unit
     * @param unit    a TimeUnit determining how to interpret the timeout parameter
     * @return the head of this queue, or null if the specified waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    /**
     * Retrieves and removes the head of this queue.
     * This method differs from poll only in that it throws an exception if this queue is empty.
     *
     * @return the head of this queue
     */
    public E remove() {
        if (size.get() != 0) {
            return poll();
        } else {
            // TODO wait
            return null;
        }
    }

    /**
     * Removes a single instance of the specified element from this queue, if it is present. More formally, removes an element e such that o.equals(e), if this queue contains one or more such elements.
     * Returns true if this queue contained the specified element
     * (or equivalently, if this queue changed as a result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return if this queue changed as a result of the call
     * @throws
     */
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Returns true if this collection contains no elements.
     *
     * @return true if this collection contains no elements
     */
    public boolean isEmpty() {
        return (size.get() == 0);
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    public int size() {
        return size.get();
    }

    /**
     * Always returns Integer.MAX_VALUE because a PriorityBlockingQueue is not capacity constrained.
     *
     * @return Integer.MAX_VALUE
     */
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     */
    public void clear() {
        initBinaryArray();
        size.set(0);
    }

    /**
     * Removes all available elements from this queue and adds them to the given collection.
     * This operation may be more efficient than repeatedly polling this queue.
     * A failure encountered while attempting to add elements to collection c may result in elements being
     * in neither, either or both collections when the associated exception is thrown.
     * Attempts to drain a queue to itself result in IllegalArgumentException. Further,
     * the behavior of this operation is undefined if the specified collection is modified
     * while the operation is in progress.
     *
     * @param c the collection to transfer elements into
     * @return the number of elements transferred
     * @throws UnsupportedOperationException if addition of elements is not supported by the specified collection
     * @throws ClassCastException            if the class of an element of this queue prevents it from being added to the specified collection
     * @throws NullPointerException          if the specified collection is null
     * @throws IllegalArgumentException      if the specified collection is this queue, or some property of an element of this queue prevents it from being added to the specified collection
     */
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    /**
     * Removes all available elements from this queue and adds them to the given collection.
     * This operation may be more efficient than repeatedly polling this queue.
     * A failure encountered while attempting to add elements to collection c may result in elements being
     * in neither, either or both collections when the associated exception is thrown.
     * Attempts to drain a queue to itself result in IllegalArgumentException. Further,
     * the behavior of this operation is undefined if the specified collection is modified
     * while the operation is in progress.
     *
     * @param c           the collection to transfer elements into
     * @param maxElements the maximum number of elements to transfer
     * @return the number of elements transferred
     * @throws UnsupportedOperationException if addition of elements is not supported by the specified collection
     * @throws ClassCastException            if the class of an element of this queue prevents it from being added to the specified collection
     * @throws NullPointerException          if the specified collection is null
     * @throws IllegalArgumentException      if the specified collection is this queue, or some property of an element of this queue prevents it from being added to the specified collection
     */
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * The returned array will be "safe" in that no references to it are maintained by this collection.
     * (In other words, this method must allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * @return an array containing all of the elements in this collection
     */
    public Object[] toArray() {
        lockAllLevels();
        Object[] result = new Object[size.get()];
        int taken = 0;
        int runner = 0;
        while (taken < size.get() && runner < binaryArray.length) {
            if (binaryArray[runner].getValue() != null) {
                result[taken] = binaryArray[runner].getValue();
                taken++;
            }
            runner++;
        }

        if (taken != size.get()) {
            throw new IllegalStateException("toArray unsuccessful");
        }
        unlockAllLevels();
        return result;
    }

    /**
     * Returns an array containing all of the elements in this queue.
     * The runtime type of the returned array is that of the specified array.
     * If the queue fits in the specified array, it is returned therein.
     * If the queue fits with unfilled elements, the element in the array immediately following the end of the collection is set to null
     * Otherwise a new array is allocated with the runtime type of the specified array and the size of this queue.
     *
     * @param a the array into which the elements of the queue are to be stored, if big enough
     * @return an array containing all of the elements in this queue
     */
    public <T> T[] toArray(T[] a) {
        lockAllLevels();
        if (a.length < size.get()) {
            return (T[])toArray();
        }

        int taken = 0;
        int runner = 0;
        while (taken < size.get() && runner <binaryArray.length) {
            if (binaryArray[runner].getValue() != null) {
                a[taken] = (T)binaryArray[taken].getValue();
                taken++;
            }
            runner++;
        }

        if (taken < a.length) {
            a[taken] = null;
        }

        if (taken != size.get()) {
            throw new IllegalStateException("toArray unsuccessful");
        }

        unlockAllLevels();
        return a;
    }

    /**
     * Returns an iterator over the elements in this queue. The
     * iterator does not return the elements in any particular order.
     * The returned <tt>Iterator</tt> is a "weakly consistent"
     * iterator that will never throw {@link
     * ConcurrentModificationException}, and guarantees to traverse
     * elements as they existed upon construction of the iterator, and
     * may (but is not guaranteed to) reflect any modifications
     * subsequent to construction.
     *
     * @return an iterator over the elements in this queue
     */
    public Iterator<E> iterator() {
        // TODO change this to match the documentation specifications
        return new Iterator<E>() {
            public boolean hasNext() {
                return peek() != null;
            }

            public E next() {
                if (peek() == null) throw new NoSuchElementException();
                boolean taken = false;
                E e = null;
                while (!taken) {
                    try {
                        e = take();
                    } catch (InterruptedException ex) {
                    }
                }
                return e;
            }

            public void remove() {
            }
        };
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     * @return <tt>true</tt> if this queue contains the specified element
     */
    public boolean contains(Object o) {
        lockAllLevels();
        for (BinaryArrayElement e : binaryArray) {
            if (e.getValue() == null) continue;
            if (e.getValue().equals(o)) return true;
        }
        unlockAllLevels();
        return false;
    }

    /**
     * Returns true if this collection contains all of the elements in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return if this collection contains all of the elements in the specified collection
     * @throws ClassCastException   if the types of one or more elements in the specified collection are incompatible with this collection
     * @throws NullPointerException if the specified collection contains one or more null elements and this collection does not permit null elements
     */
    public boolean containsAll(Collection<?> c) {
        lockAllLevels();
        if (c.size() > size.get()) return false;

        HashSet set1 = new HashSet(c);
        int count = 0;
        for (BinaryArrayElement e : binaryArray) {
            if (e.getValue() == null) continue;
            if (set1.contains(e.getValue())) count++;
        }
        unlockAllLevels();
        return set1.size() == count;
    }

    /**
     * <p>This implementation iterates over the specified collection, and adds
     * each object returned by the iterator to this collection, in turn.
     * <p>
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> unless <tt>add</tt> is
     * overridden (assuming the specified collection is non-empty).
     *
     * @throws UnsupportedOperationException if the addAll operation is not supported by this collection
     * @throws ClassCastException            if the class of an element of the specified collection prevents it from being added to this collection
     * @throws NullPointerException          if the specified collection contains a null element and this collection does not permit null elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the specified collection prevents it from being added to this collection
     * @throws IllegalStateException         if not all the elements can be added at this time due to insertion restrictions
     * @see #add(Object)
     * @return true
     */
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    /**
     *
     * <p>This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's so contained, it's removed from
     * this collection with the iterator's <tt>remove</tt> method.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by the
     * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
     * and this collection contains one or more elements in common with the
     * specified collection.
     *
     * @throws UnsupportedOperationException if the removeAll method is not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this collection are incompatible with the specified collection
     * @throws NullPointerException if this collection contains one or more null elements and the specified collection does not support null elements, or if the specified collection is null
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * <p>This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's not so contained, it's removed
     * from this collection with the iterator's <tt>remove</tt> method.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by the
     * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
     * and this collection contains one or more elements not present in the
     * specified collection.
     *
     * @throws UnsupportedOperationException  if the retainAll operation is not supported by this collection
     * @throws ClassCastException   if the types of one or more elements in this collection are incompatible with the specified collection
     * @throws NullPointerException  if this collection contains one or more null elements and the specified collection does not permit null elements, or if the specified collection is null
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    private boolean localEnqueue(int i) {
        int position = tokenArray[i].getPosition();
        E value = tokenArray[i].getValue();

        BinaryArrayElement<E> element = binaryArray[position];

        if (!element.isActive()) {
            element.setValue(value);
            element.setActive(true);
            element.decrementCapacity();
            return true;
        } else if (tokenArray[i].isGreaterThan(binaryArray[position].getValue())) {
            E temp = tokenArray[i].getValue();
            tokenArray[i].setValue(binaryArray[position].getValue());
            element.setValue(temp);
        }

        element.decrementCapacity();
        tokenArray[i + 1].setValue(tokenArray[i].getValue());
        tokenArray[i].setValue(null);

        BinaryArrayElement<E> left = getLeft(position);
        BinaryArrayElement<E> right = getRight(position);
        if (left == null && right == null) {
            return true;//check
        }

        int next;
        if (left == null) {
            next = getRightIndex(position);
        } else if (right == null) {
            next = getLeftIndex(position);
        } else if (left.getValue() == null) {
            next = getLeftIndex(position);
        } else if (right.getValue() == null) {
            next = getRightIndex(position);
        } else if (left.getCapacity() > right.getCapacity()) {
            next = getLeftIndex(position);
        } else {
            next = getRightIndex(position);
        }
        tokenArray[i + 1].setPosition(next);

        return false;
    }

    private boolean localDequeue(int i) {
        int current = tokenArray[i].getPosition();
        BinaryArrayElement<E> leftChild = getLeft(current);
        BinaryArrayElement<E> rightChild = getRight(current);

        if ((leftChild == null || !leftChild.isActive())
                && (rightChild == null || !rightChild.isActive())) {
            return true;
        }

        BinaryArrayElement<E> greatestChild;
        int greatestChildPosition;

        if (leftChild == null || leftChild.getValue() == null) {
            greatestChild = rightChild;
            greatestChildPosition = getRightIndex(current);
        } else if (rightChild == null || rightChild.getValue() == null || leftChild.isGreaterThan(rightChild.getValue())) {
            greatestChild = leftChild;
            greatestChildPosition = getLeftIndex(current);
        } else {
            greatestChild = rightChild;
            greatestChildPosition = getRightIndex(current);
        }

        binaryArray[current].setActive(true);
        binaryArray[current].setValue(greatestChild.getValue());
        greatestChild.setActive(false);
        greatestChild.setValue(null);
        greatestChild.incrementCapacity();
        tokenArray[i + 1].setPosition(greatestChildPosition);
        return false;
    }

    private void checkCapacity() {
        if (getRoot().getCapacity() < 1) {
            int newSize = binaryArray.length * 2;
            BinaryArrayElement[] temp = new BinaryArrayElement[newSize];

            int elementPos = 1;
            for (BinaryArrayElement e : binaryArray) {
                e.setCapacity(e.getCapacity() * 2); //needs fixing
                temp[elementPos] = e;
                elementPos++;
            }

            binaryArray = temp;
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

    /**
     * Increases the capacity of the array.
     */
    private void resize() {
        int tokenArrayLength = tokenArray.length;
        for (int i = 0; i < tokenArrayLength; i ++) {
            tokenArray[i].lock();
        }

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

        updateCapacities(0);
        //resize token array same as current
        this.treeHeight = BinaryTreeUtils.convertSizeToNumLevels(newCapacity);

        TokenArrayElement<E>[] temp = new TokenArrayElement[treeHeight];
        for (int i=0; i<tokenArray.length; i++){
            temp[i] = tokenArray[i];
        }

        for (int i=tokenArray.length; i<treeHeight; i++){
            temp[i] = new TokenArrayElement(null, null, 1, comparator, new ReentrantLock(true));
        }
        tokenArray = temp;

        for (int i = 2; i < tokenArrayLength; i++) {
            tokenArray[i].unlock();
        }
    }

    private int getLevelOfIndex(int index) {
        return (int) Math.floor(Math.log(index + 1) / Math.log(2)) + 1;
    }

    private void updateCapacities(int i) {
        if (i < 0 || i >= binaryArray.length) return;
        updateCapacities(getLeftIndex(i));
        updateCapacities(getRightIndex(i));

        int capacity = binaryArray[i].isActive() ? 0 : 1;

        BinaryArrayElement<E> leftChild = getLeft(i);
        if (leftChild != null) {
            capacity += leftChild.getCapacity();
        }

        BinaryArrayElement<E> rightChild = getRight(i);
        if (rightChild != null) {
            capacity += rightChild.getCapacity();
        }

        binaryArray[i].setCapacity(capacity);
    }

    private void lockAllLevels() {
        for (TokenArrayElement tae : tokenArray) tae.lock();
    }

    private void unlockAllLevels() {
        for (TokenArrayElement tae : tokenArray) tae.unlock();
    }

    @Override // TODO update to reflect new fields
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PipelinedPriorityQueue<?> that = (PipelinedPriorityQueue<?>) o;

        return new EqualsBuilder()
                .append(binaryArray, that.binaryArray)
                .append(treeHeight, that.treeHeight)
                .append(comparator, that.comparator)
                .isEquals();
    }

    @Override // TODO update to reflect new fields
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(binaryArray)
                .append(treeHeight)
                .append(comparator)
                .toHashCode();
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
