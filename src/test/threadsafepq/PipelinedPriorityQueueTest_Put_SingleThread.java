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
    public void Put_InOrderNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        defaultQueue.put(0);
        defaultQueue.put(1);
        defaultQueue.put(2);
        defaultQueue.put(3);
        defaultQueue.put(4);

        Object[] queueArray = defaultQueue.toArray();
        int[] expected = {0,1,2,3,4};
        assertEquals(5, queueArray.length);
        compareAll(queueArray, expected);
    }

    @Test
    public void Put_InOrderNoResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        capacityQueue.put(0);
        capacityQueue.put(1);
        capacityQueue.put(2);
        capacityQueue.put(3);
        capacityQueue.put(4);

        Object[] queueArray = capacityQueue.toArray();
        int[] expected = {0,1,2,3,4};
        assertEquals(5, queueArray.length);
        compareAll(queueArray, expected);
    }

    @Test
    public void Put_InOrderNoResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        capacityComparatorQueue.put(4);
        capacityComparatorQueue.put(3);
        capacityComparatorQueue.put(2);
        capacityComparatorQueue.put(1);
        capacityComparatorQueue.put(0);

        Object[] queueArray = capacityComparatorQueue.toArray();
        int[] expected = {4,3,2,1,0};
        assertEquals(5, queueArray.length);
        compareAll(queueArray, expected);
    }

    @Test
    public void Put_InOrderSingleResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 13;
        for (int i = 0; i < queueArrayLength; i++) {
            defaultQueue.put(i);
        }

        Object[] queueArray = defaultQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        compareAll(expected, defaultQueue);
    }

    @Test
    public void Put_InOrderSingleResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 13;
        for (int i = 0; i < queueArrayLength; i++) {
            capacityQueue.put(i);
        }

        Object[] queueArray = capacityQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        compareAll(expected, capacityQueue);
    }

    @Test
    public void Put_InOrderSingleResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 13;
        for (int i = 0; i < queueArrayLength; i++) {
            capacityComparatorQueue.put(i);
        }

        Object[] queueArray = capacityComparatorQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {12,11,10,9,8,7,6,5,4,3,2,1,0};
        compareAll(expected, capacityComparatorQueue);
    }

    @Test
    public void Put_ReverseOrderNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 5;
        for (int i = 4; i >= 0 ; i--) {
            defaultQueue.put(i);
        }

        Object[] queueArray = defaultQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {0,1,2,3,4};
        compareAll(expected, defaultQueue);
    }

    @Test
    public void Put_ReverseOrderNoResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 5;
        for (int i = 4; i >= 0 ; i--) {
            capacityQueue.put(i);
        }

        Object[] queueArray = capacityQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {0,1,2,3,4};
        compareAll(expected, capacityQueue);
    }

    @Test
    public void Put_ReverseOrderNoResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 5;
        for (int i = 4; i >= 0 ; i--) {
            capacityComparatorQueue.put(i);
        }

        Object[] queueArray = capacityComparatorQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {4,3,2,1,0};
        compareAll(expected, capacityComparatorQueue);
    }

    @Test
    public void Put_ReverseOrderSingleResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 13;
        for (int i = 12; i >= 0 ; i--) {
            defaultQueue.put(i);
        }

        Object[] queueArray = defaultQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        compareAll(expected, defaultQueue);
    }

    @Test
    public void Put_ReverseOrderSingleResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 13;
        for (int i = 12; i >= 0 ; i--) {
            capacityQueue.put(i);
        }

        Object[] queueArray = capacityQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        compareAll(expected, capacityQueue);
    }

    @Test
    public void Put_ReverseOrderSingleResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int queueArrayLength = 13;
        for (int i = 12; i >= 0 ; i--) {
            capacityComparatorQueue.put(i);
        }

        Object[] queueArray = capacityComparatorQueue.toArray();

        assertEquals(queueArrayLength, queueArray.length);
        int[] expected = {12,11,10,9,8,7,6,5,4,3,2,1,0};
        compareAll(expected, capacityComparatorQueue);
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

    private void compareAll(int[] expected, PipelinedPriorityQueue<Integer> q) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int) q.remove());
        }
    }

    private void compareAll(Object[] input, int[] expected) {
        for (int i = 0; i < input.length; i++) {
            assertEquals(expected[i], (int) input[i]);
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