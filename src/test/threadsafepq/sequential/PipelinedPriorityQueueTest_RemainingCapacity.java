package threadsafepq.sequential;

import org.junit.Test;
import threadsafepq.PipelinedPriorityQueue;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueueTest_RemainingCapacity extends PipelinedPriorityQueueTest {

    @Test
    public void RemainingCapacity_DefaultQueueEmpty_ReturnsMaxInteger() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        assertEquals(Integer.MAX_VALUE, queue.remainingCapacity());
    }

    @Test
    public void RemainingCapacity_DefaultQueueNonEmpty_ReturnsMaxInteger() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        assertEquals(Integer.MAX_VALUE, queue.remainingCapacity());
    }

    @Test
    public void RemainingCapacity_CapacityQueueEmpty_ReturnsMaxInteger() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        assertEquals(Integer.MAX_VALUE, queue.remainingCapacity());
    }

    @Test
    public void RemainingCapacity_CapacityQueueNonEmpty_ReturnsMaxInteger() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        assertEquals(Integer.MAX_VALUE, queue.remainingCapacity());
    }

    @Test
    public void RemainingCapacity_CapacityComparatorQueueEmpty_ReturnsMaxInteger() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        assertEquals(Integer.MAX_VALUE, queue.remainingCapacity());
    }

    @Test
    public void RemainingCapacity_CapacityComparatorQueueNonEmpty_ReturnsMaxInteger() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        assertEquals(Integer.MAX_VALUE, queue.remainingCapacity());
    }

}
