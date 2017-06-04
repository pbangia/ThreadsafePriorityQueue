package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueue_IsEmpty_SingleThread {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int CUSTOM_INITIAL_CAPACITY = 42;
    private PipelinedPriorityQueue<Integer> defaultQueue;
    private PipelinedPriorityQueue<Integer> capacityQueue;
    private PipelinedPriorityQueue<Integer> capacityComparatorQueue;

    @Before
    public void before() {
        defaultQueue = new PipelinedPriorityQueue<Integer>();
        capacityQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY);
        capacityComparatorQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY, (o1, o2) -> o1.compareTo(o2));
    }

    @Test
    public void IsEmpty_DefaultQueueWithNoElements_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        assertEquals(true, queue.isEmpty());
    }

    @Test
    public void IsEmpty_DefaultQueueWithOneElement_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        assertEquals(false, queue.isEmpty());
    }

    @Test
    public void IsEmpty_DefaultQueueWithMultipleElements_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        queue.put(2);
        queue.put(3);
        assertEquals(false, queue.isEmpty());
    }

    @Test
    public void IsEmpty_DefaultQueueInsertThenRemove_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        queue.remove();
        assertEquals(true, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityQueueWithNoElements_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        assertEquals(true, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityQueueWithOneElement_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        assertEquals(false, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityQueueWithMultipleElements_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        queue.put(2);
        queue.put(3);
        assertEquals(false, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityQueueInsertThenRemove_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        queue.remove();
        assertEquals(true, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityComparatorQueueWithNoElements_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        assertEquals(true, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityComparatorQueueWithOneElement_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        assertEquals(false, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityComparatorQueueWithMultipleElements_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        queue.put(2);
        queue.put(3);
        assertEquals(false, queue.isEmpty());
    }

    @Test
    public void IsEmpty_CapacityComparatorQueueInsertThenRemove_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        queue.remove();
        assertEquals(true, queue.isEmpty());
    }
}
