package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taranpreet on 27/05/2017.
 */
public class PipelinedPriorityQueueTest_Put_SingleThread {

    private PipelinedPriorityQueue<Integer> defaultQueue;
    private PipelinedPriorityQueue<Integer> capacityQueue;
    private PipelinedPriorityQueue<Integer> capacityComparatorQueue;
    private int initialCapacity = 42;

    @Before
    public void before() {
        defaultQueue = new PipelinedPriorityQueue<Integer>();
        capacityQueue = new PipelinedPriorityQueue<>(initialCapacity);
        capacityComparatorQueue = new PipelinedPriorityQueue<>(initialCapacity, (o1, o2) -> {
            int result = o1.compareTo(o2);
            if (result == -1 ) return 1;
            if (result == 1) return -1;
            return result;
        });
    }

    @Test
    public void Put_SingleNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        defaultQueue.put(1);

        Object[] queueArray = defaultQueue.toArray();
        assertEquals(1, queueArray.length);
        assertEquals(1, (int) (Integer) queueArray[0]);
    }

    @Test
    public void Put_SingleNoResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
           capacityQueue.put(1);

           Object[] queueArray = capacityQueue.toArray();
           assertEquals(1, queueArray.length);
           assertEquals(1, (int) (Integer) queueArray[0]);
    }

    @Test
    public void Put_SingleNoResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        capacityComparatorQueue.put(1);

        Object[] queueArray = capacityComparatorQueue.toArray();
        assertEquals(1, queueArray.length);
        assertEquals(1, (int) (Integer) queueArray[0]);
    }
    

    @Test
    public void Put_DefaultQueueMultipleItems_CorrectEnqueues() throws InterruptedException {
        defaultQueue.put(1);
        defaultQueue.put(0);
        defaultQueue.put(3);
        defaultQueue.put(2);
        defaultQueue.put(5);

        Object[] queueArray = defaultQueue.toArray();
        assertEquals(5, queueArray.length);
        assertEquals(0, (int)defaultQueue.remove());
        assertEquals(1, (int)defaultQueue.remove());
        assertEquals(2, (int)defaultQueue.remove());
        assertEquals(3, (int)defaultQueue.remove());
        assertEquals(5, (int)defaultQueue.remove());
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
            defaultQueue.put(ordering[i]);
        }

        Object[] queueArray = defaultQueue.toArray();
        assertEquals(arraySize, queueArray.length);
        Arrays.sort(ordering);
        for (int i = 0; i < arraySize; i++) {
            int removed = defaultQueue.remove();
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