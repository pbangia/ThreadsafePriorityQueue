package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Taranpreet on 29/05/2017.
 */
public class PipelinedPriorityQueueTest_Remove_SingleThread {

    private PipelinedPriorityQueue<Integer> queue;

    @Before
    public void before() {
        queue = new PipelinedPriorityQueue<Integer>();
    }

    @Test
    public void Remove_SingleItem_CorrectlyDequeuesItem() throws InterruptedException {
        queue.put(1);
        int result = queue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Remove_SingleItem2_CorrectlyDequeuesItem() throws InterruptedException {
        queue.put(1);
        queue.put(0);
        int result = queue.remove();

        assertEquals(0, result);
    }

    @Test
    public void Remove_SingleItem3_CorrectlyDequeuesItem() throws InterruptedException {
        queue.put(0);
        queue.put(1);
        int result = queue.remove();

        assertEquals(0, result);
        result = queue.remove();
        assertEquals(1, result);
    }

    @Test
    public void Remove_ClearAll() {
        queue.put(0);
        queue.put(1);
        assertFalse(queue.isEmpty());
        queue.clear();
        assertTrue(queue.isEmpty());
    }

}
