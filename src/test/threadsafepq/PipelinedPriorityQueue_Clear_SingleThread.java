package threadsafepq;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PipelinedPriorityQueue_Clear_SingleThread extends PipelinedPriorityQueueTest {

    @Test
    public void Clear_DefaultQueueEmptyQueueThenClear_ClearsAllElements() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.clear();
        assertEquals(0, queue.toArray().length);
    }

    @Test
    public void Clear_DefaultQueueNonEmptyQueueThenClear_ClearsAllElements() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(1);
        queue.put(3);
        queue.remove();
        queue.clear();
        assertEquals(0, queue.toArray().length);
    }

    @Test
    public void Clear_CapacityQueueEmptyQueueThenClear_ClearsAllElements() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.clear();
        assertEquals(0, queue.toArray().length);
    }

    @Test
    public void Clear_CapacityQueueNonEmptyQueueThenClear_ClearsAllElements() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(1);
        queue.put(3);
        queue.remove();
        queue.clear();
        assertEquals(0, queue.toArray().length);
    }

    @Test
    public void Clear_CapacityComparatorQueueEmptyQueueThenClear_ClearsAllElements() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.clear();
        assertEquals(0, queue.toArray().length);
    }

    @Test
    public void Clear_CapacityComparatorQueueNonEmptyQueueThenClear_ClearsAllElements() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(1);
        queue.put(3);
        queue.remove();
        queue.clear();
        assertEquals(0, queue.toArray().length);
    }


}
