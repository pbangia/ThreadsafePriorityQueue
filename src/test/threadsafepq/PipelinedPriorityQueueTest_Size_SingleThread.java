package threadsafepq;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueueTest_Size_SingleThread extends PipelinedPriorityQueueTest {

    @Test
    public void Size_DefaultQueueEmpty_ReturnsZero() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        assertEquals(0, queue.size());
    }

    @Test
    public void Size_DefaultQueueSingleItem_ReturnsOne() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        assertEquals(1, queue.size());
    }

    @Test
    public void Size_DefaultQueueMultipleItems_ReturnsCorrectSize() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        queue.put(1);
        queue.put(1);
        assertEquals(3, queue.size());
    }

    @Test
    public void Size_DefaultQueueInsertsThenRemoves_ReturnsCorrectSize() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        queue.put(1);
        queue.put(1);
        queue.remove();
        assertEquals(2, queue.size());
    }

    @Test
    public void Size_CapacityQueueEmpty_ReturnsZero() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        assertEquals(0, queue.size());
    }

    @Test
    public void Size_CapacityQueueSingleItem_ReturnsOne() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        assertEquals(1, queue.size());
    }

    @Test
    public void Size_CapacityQueueMultipleItems_ReturnsCorrectSize() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        queue.put(1);
        queue.put(1);
        assertEquals(3, queue.size());
    }

    @Test
    public void Size_CapacityQueueInsertsThenRemoves_ReturnsCorrectSize() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        queue.put(1);
        queue.put(1);
        queue.remove();
        assertEquals(2, queue.size());
    }

    @Test
    public void Size_CapacityComparatorQueueEmpty_ReturnsZero() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        assertEquals(0, queue.size());
    }

    @Test
    public void Size_CapacityComparatorQueueSingleItem_ReturnsOne() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        assertEquals(1, queue.size());
    }

    @Test
    public void Size_CapacityComparatorQueueMultipleItems_ReturnsCorrectSize() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        queue.put(1);
        queue.put(1);
        assertEquals(3, queue.size());
    }

    @Test
    public void Size_CapacityComparatorQueueInsertsThenRemoves_ReturnsCorrectSize() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        queue.put(1);
        queue.put(1);
        queue.remove();
        assertEquals(2, queue.size());
    }

}
