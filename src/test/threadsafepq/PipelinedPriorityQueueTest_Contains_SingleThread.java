package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Taranpreet on 3/06/2017.
 */
public class PipelinedPriorityQueueTest_Contains_SingleThread {

    private PipelinedPriorityQueue<Integer> queue;

    @Before
    public void before() {
        queue = new PipelinedPriorityQueue<Integer>();
    }

    @Test
    public void Put_DefaultQueueMultipleItems3_CorrectContains() {
        for (int i = 0; i < 6; i++) {
            assertFalse(queue.contains(i));
        }

        queue.put(0);
        queue.put(2);
        queue.put(5);
        queue.put(1);
        queue.put(3);
        queue.put(4);

        for (int i = 0; i < 6; i++) {
            assertTrue(queue.contains(i));
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(0);
        list.add(1);
        list.add(5);
        assertTrue(queue.containsAll(list));
        list.remove(0);
        list.remove(1);
        assertTrue(queue.containsAll(list));

        list.add(8);
        assertFalse(queue.containsAll(list));
    }

}
