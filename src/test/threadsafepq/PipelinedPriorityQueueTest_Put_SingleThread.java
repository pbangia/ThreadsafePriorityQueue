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

    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int CUSTOM_INITIAL_CAPACITY = 42;

    @Before
    public void before() {
        defaultQueue = new PipelinedPriorityQueue<Integer>();
        capacityQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY);
        capacityComparatorQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY, (o1, o2) -> {
            int result = o1.compareTo(o2);
            if (result == -1 ) return 1;
            if (result == 1) return -1;
            return result;
        });
    }

    @Test
    public void Put_SingleNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        defaultQueue.put(1);
        int result = defaultQueue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Put_SingleNoResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        capacityQueue.put(1);
        int result = capacityQueue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Put_SingleNoResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        capacityComparatorQueue.put(1);
        int result = capacityComparatorQueue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Put_InOrderNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = {0, 1, 2, 3, 4, 5};
        int[] outputList = {0, 1, 2, 3, 4, 5};

        testPutWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderNoResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = {0, 1, 2, 3, 4, 5};
        int[] outputList = {0, 1, 2, 3, 4, 5};

        testPutWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderNoResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {5, 4, 3, 2, 1};

        testPutWithInputList(capacityComparatorQueue, inputList, outputList);
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

    private void testPutWithInputList(PipelinedPriorityQueue<Integer> queue,
                                      int[] inputList, int[] expectedOutput) {
        for (int i : inputList) {
            queue.put(i);
        }

        for (int i : expectedOutput) {
            int out = queue.remove();
            assertEquals(i, out);
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