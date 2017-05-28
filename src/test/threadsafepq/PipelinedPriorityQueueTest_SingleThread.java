package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taranpreet on 27/05/2017.
 */
public class PipelinedPriorityQueueTest_SingleThread {

    private PipelinedPriorityQueue<Integer> queue;

    @Before
    public void before() {
        queue = new PipelinedPriorityQueue<Integer>();
    }

    @Test
    public void Put_EmptyQueue_CorrectlyEnqueuesItem() throws InterruptedException {
        queue.put(1);

        Object[] queueArray = queue.toArray();
        assertEquals(1, queueArray.length);
        assertEquals(1, (int) (Integer) queueArray[0]);
    }

    @Test
    public void Put_MultipleTimes_CorrectlyEnqueuesItems() throws InterruptedException {
        queue.put(1);
        queue.put(0);
        queue.put(3);
        queue.put(2);
        queue.put(5);

        Object[] queueArray = queue.toArray();
        assertEquals(5, queueArray.length);
        assertEquals(0, queueArray[0]);
        assertEquals(1, queueArray[1]);
        assertEquals(2, queueArray[2]);
        assertEquals(3, queueArray[3]);
        assertEquals(5, queueArray[4]);
    }

    @Test
    public void Put_MultipleTimes2_CorrectlyEnqueuesItems() throws InterruptedException {
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
        for (int i = 0; i < arraySize; i++) {
            assertEquals(ordering[i], (int) (Integer) queueArray[i]);
        }
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