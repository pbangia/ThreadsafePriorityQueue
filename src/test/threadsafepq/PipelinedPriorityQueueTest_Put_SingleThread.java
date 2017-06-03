package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taranpreet on 27/05/2017.
 */
public class PipelinedPriorityQueueTest_Put_SingleThread {

    private PipelinedPriorityQueue<Integer> queue;

    @Before
    public void before() {
        queue = new PipelinedPriorityQueue<Integer>();
    }

    @Test
    public void Put_SingleNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        queue.put(1);

        Object[] queueArray = queue.toArray();
        assertEquals(1, queueArray.length);
        assertEquals(1, (int) (Integer) queueArray[0]);
    }

    @Test
    public void Put_DefaultQueueMultipleItems_CorrectEnqueues() throws InterruptedException {
        queue.put(1);
        queue.put(0);
        queue.put(3);
        queue.put(2);
        queue.put(5);

        Object[] queueArray = queue.toArray();
        assertEquals(5, queueArray.length);
        assertEquals(0, (int)queue.remove());
        assertEquals(1, (int)queue.remove());
        assertEquals(2, (int)queue.remove());
        assertEquals(3, (int)queue.remove());
        assertEquals(5, (int)queue.remove());
    }

    @Test
    public void Put_DefaultQueueMultipleItems2_CorrectEnqueues() throws InterruptedException {
        int arraySize = 1000;
        int[] ordering = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            ordering[i] = i;
        }
        shuffleArray(ordering);

        for (int i = 0; i < arraySize; i++) {
            queue.put(ordering[i]);
        }

        Object[] queueArray = queue.toArray();
        assertEquals(arraySize, queueArray.length);
        Arrays.sort(ordering);
        for (int i = 0; i < arraySize; i++) {
            int removed = queue.remove();
            assertEquals(ordering[i], removed);
        }
    }

    @Test
    public void Put_DefaultQueueOrderedItems_CorrectEnqueues() throws InterruptedException {

    }

    @Test
    public void Put_DefaultQueueReverseOrderedItems_CorrectEnqueues() throws InterruptedException {

    }

    private void shuffleArray(int[] array) {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }
}