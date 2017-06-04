package threadsafepq.sequential;

import org.junit.Test;
import threadsafepq.PipelinedPriorityQueue;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueue_Element extends PipelinedPriorityQueueTest {

    @Test(expected = NoSuchElementException.class)
    public void Peek_DefaultQueueEmptyQueue_ThrowsException() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.element();
    }

    @Test
    public void Peek_DefaultQueueSingleItemQueue_ReturnsItem() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        assertEquals(1, (int) queue.element());
    }

    @Test
    public void Peek_DefaultQueueMultipleItemsQueue_ReturnsCorrectTop() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(2);
        queue.put(3);
        assertEquals(2, (int) queue.element());
    }

    @Test(expected = NoSuchElementException.class)
    public void Peek_CapacityQueueEmptyQueue_ThrowsException() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.element();
    }

    @Test
    public void Peek_CapacityQueueSingleItemQueue_ReturnsItem() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        assertEquals(1, (int) queue.element());
    }

    @Test
    public void Peek_CapacityQueueMultipleItemsQueue_ReturnsCorrectTop() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(2);
        queue.put(3);
        assertEquals(2, (int) queue.element());
    }

    @Test(expected = NoSuchElementException.class)
    public void Peek_CapacityComparatorQueueEmptyQueue_ThrowsException() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.element();
    }

    @Test
    public void Peek_CapacityComparatorQueueSingleItemQueue_ReturnsItem() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        assertEquals(1, (int) queue.element());
    }

    @Test
    public void Peek_CapacityComparatorQueueMultipleItemsQueue_ReturnsCorrectTop() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(2);
        queue.put(3);
        assertEquals(2, (int) queue.element());
    }
}
