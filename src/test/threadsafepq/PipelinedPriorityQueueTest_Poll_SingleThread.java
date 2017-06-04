package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PipelinedPriorityQueueTest_Poll_SingleThread {
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int CUSTOM_INITIAL_CAPACITY = 42;
    private PipelinedPriorityQueue<Integer> defaultQueue;
    private PipelinedPriorityQueue<Integer> capacityQueue;
    private PipelinedPriorityQueue<Integer> capacityComparatorQueue;

    @Before
    public void before() {
        defaultQueue = new PipelinedPriorityQueue<>();
        capacityQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY);
        capacityComparatorQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY, (o1, o2) -> {
            int actual = o1.compareTo(o2);
            if (actual == -1) return 1;
            if (actual == 1) return -1;
            return 0;
        });
    }

    @Test
    public void Poll_NoItemsDefaultQueue_ReturnsNull() {
        assertNull(defaultQueue.poll());
    }

    @Test
    public void Poll_NoItemsCapacityQueue_ReturnsNull() {
        assertNull(capacityQueue.poll());
    }

    @Test
    public void Poll_NoItemsCapacityComparatorQueue_ReturnsNull() {
        assertNull(capacityComparatorQueue.poll());
    }

    @Test
    public void Poll_SingleItemDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        defaultQueue.put(1);
        int result = defaultQueue.poll();

        assertEquals(1, result);
    }

    @Test
    public void Poll_SingleItemCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        capacityQueue.put(1);
        int result = capacityQueue.poll();

        assertEquals(1, result);
    }

    @Test
    public void Poll_SingleItemCapacityComparatorQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        capacityComparatorQueue.put(1);
        int result = capacityComparatorQueue.poll();

        assertEquals(1, result);
    }

    @Test
    public void Poll_InOrderDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {0, 1, 2, 3, 4, 5};
        int[] outputList = {0, 1, 2, 3, 4, 5};

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {0, 1, 2, 3, 4, 5};
        int[] outputList = {0, 1, 2, 3, 4, 5};

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderCapacityComparatorNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {5, 4, 3, 2, 1};

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderDefaultQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderCapacityQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderCapacityComparatorQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderDefaultQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderCapacityQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_InOrderCapacityComparatorQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    // break

    @Test
    public void Poll_ReverseOrderDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_ReverseOrderCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_ReverseOrderCapacityComparatorNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {1, 2, 3, 4, 5};
        int[] outputList = {5, 4, 3, 2, 1};

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Poll_ReverseOrderDefaultQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_ReverseOrderCapacityQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_ReverseOrderCapacityComparatorQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
            outputList[i] = inputList.length - i - 1;
        }

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Poll_ReverseOrderDefaultQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    // break

    @Test
    public void Poll_RandomOrderDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        shuffleArray(inputList);
        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        shuffleArray(inputList);
        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderCapacityComparatorNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {1, 2, 3, 4, 5};
        int[] outputList = {5, 4, 3, 2, 1};

        shuffleArray(inputList);
        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderDefaultQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        shuffleArray(inputList);
        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderCapacityQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        shuffleArray(inputList);
        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderCapacityComparatorQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
            outputList[i] = inputList.length - i - 1;
        }

        shuffleArray(inputList);
        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderDefaultQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        shuffleArray(inputList);
        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderCapacityQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        shuffleArray(inputList);
        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Poll_RandomOrderCapacityComparatorQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
            outputList[i] = inputList.length - i - 1;
        }

        shuffleArray(inputList);
        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    private void testRemoveWithInputList(PipelinedPriorityQueue<Integer> queue,
                                         int[] inputList, int[] expectedOutput) {
        for (int i : inputList) {
            queue.put(i);
        }

        for (int i : expectedOutput) {
            int out = queue.poll();
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
