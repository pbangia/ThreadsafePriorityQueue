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
    public void Put_InOrderSingleResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testPutWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderSingleResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testPutWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderSingleResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testPutWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderMultipleResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testPutWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderMultipleResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testPutWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Put_InOrderMultipleResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testPutWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    // Reverse Order tests
    @Test
    public void Put_ReverseOrderNoResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        testPutWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Put_ReverseOrderNoResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        testPutWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Put_ReverseOrderNoResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = {1, 2, 3, 4, 5};
        int[] outputList = {5, 4, 3, 2, 1};

        testPutWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Put_ReverseOrderSingleResizeDefaultQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testPutWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Put_ReverseOrderSingleResizeCapacityQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testPutWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Put_ReverseOrderSingleResizeCapacityComparatorQueue_CorrectEnqueue() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
            outputList[i] = inputList.length - i - 1;
        }

        testPutWithInputList(capacityComparatorQueue, inputList, outputList);
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