package threadsafepq;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueueTest_IsEmpty_SingleThread extends PipelinedPriorityQueueTest {

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
