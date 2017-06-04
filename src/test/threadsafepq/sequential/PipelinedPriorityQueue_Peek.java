package threadsafepq.sequential;

import org.junit.Test;
import threadsafepq.PipelinedPriorityQueue;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueue_Peek extends PipelinedPriorityQueueTest {

    @Test
    public void Peek_DefaultQueueEmptyQueue_ReturnsNull() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        assertEquals(null, queue.peek());
    }

    @Test
    public void Peek_DefaultQueueSingleItemQueue_ReturnsItem() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        assertEquals(1, (int) queue.peek());
    }

    @Test
    public void Peek_DefaultQueueMultipleItemsQueue_ReturnsCorrectTop() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(2);
        queue.put(3);
        assertEquals(2, (int) queue.peek());
    }

    @Test
    public void Peek_CapacityQueueEmptyQueue_ReturnsNull() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        assertEquals(null, queue.peek());
    }

    @Test
    public void Peek_CapacityQueueSingleItemQueue_ReturnsItem() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        assertEquals(1, (int) queue.peek());
    }

    @Test
    public void Peek_CapacityQueueMultipleItemsQueue_ReturnsCorrectTop() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(2);
        queue.put(3);
        assertEquals(2, (int) queue.peek());
    }

    @Test
    public void Peek_CapacityComparatorQueueEmptyQueue_ReturnsNull() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        assertEquals(null, queue.peek());
    }

    @Test
    public void Peek_CapacityComparatorQueueSingleItemQueue_ReturnsItem() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        assertEquals(1, (int) queue.peek());
    }

    @Test
    public void Peek_CapacityComparatorQueueMultipleItemsQueue_ReturnsCorrectTop() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(2);
        queue.put(3);
        assertEquals(2, (int) queue.peek());
    }
}
