package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taranpreet on 29/05/2017.
 */
public class PipelinedPriorityQueueTest_Remove_SingleThread {

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

    @Test(expected = NoSuchElementException.class)
    public void Remove_NoItemsDefaultQueue_ReturnsNull() {
        defaultQueue.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void Remove_NoItemsCapacityQueue_ReturnsNull() {
        capacityQueue.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void Remove_NoItemsCapacityComparatorQueue_ReturnsNull() {
        capacityComparatorQueue.remove();
    }

    @Test
    public void Remove_SingleItemDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        defaultQueue.put(1);
        int result = defaultQueue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Remove_SingleItemCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        capacityQueue.put(1);
        int result = capacityQueue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Remove_SingleItemCapacityComparatorQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        capacityComparatorQueue.put(1);
        int result = capacityComparatorQueue.remove();

        assertEquals(1, result);
    }

    @Test
    public void Remove_InOrderDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {0, 1, 2, 3, 4, 5};
        int[] outputList = {0, 1, 2, 3, 4, 5};

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {0, 1, 2, 3, 4, 5};
        int[] outputList = {0, 1, 2, 3, 4, 5};

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderCapacityComparatorNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {5, 4, 3, 2, 1};

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderDefaultQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderCapacityQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderCapacityComparatorQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderDefaultQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderCapacityQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Remove_InOrderCapacityComparatorQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY * 5];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i;
        }
        int[] outputList = Arrays.copyOf(inputList, inputList.length);

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    // break

    @Test
    public void Remove_ReverseOrderDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Remove_ReverseOrderCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Remove_ReverseOrderCapacityComparatorNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {1, 2, 3, 4, 5};
        int[] outputList = {5, 4, 3, 2, 1};

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Remove_ReverseOrderDefaultQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[DEFAULT_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Remove_ReverseOrderCapacityQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = inputList.length - i - 1;
            outputList[i] = i;
        }

        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Remove_ReverseOrderCapacityComparatorQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = new int[CUSTOM_INITIAL_CAPACITY + 5];
        int[] outputList = new int[inputList.length];
        for (int i = 0; i < inputList.length; i++) {
            inputList[i] = i;
            outputList[i] = inputList.length - i - 1;
        }

        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Remove_ReverseOrderDefaultQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
    public void Remove_RandomOrderDefaultQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        shuffleArray(inputList);
        testRemoveWithInputList(defaultQueue, inputList, outputList);
    }

    @Test
    public void Remove_RandomOrderCapacityQueueNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {5, 4, 3, 2, 1};
        int[] outputList = {1, 2, 3, 4, 5};

        shuffleArray(inputList);
        testRemoveWithInputList(capacityQueue, inputList, outputList);
    }

    @Test
    public void Remove_RandomOrderCapacityComparatorNoResize_CorrectlyDequeuesItem() throws InterruptedException {
        int[] inputList = {1, 2, 3, 4, 5};
        int[] outputList = {5, 4, 3, 2, 1};

        shuffleArray(inputList);
        testRemoveWithInputList(capacityComparatorQueue, inputList, outputList);
    }

    @Test
    public void Remove_RandomOrderDefaultQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
    public void Remove_RandomOrderCapacityQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
    public void Remove_RandomOrderCapacityComparatorQueueSingleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
    public void Remove_RandomOrderDefaultQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
    public void Remove_RandomOrderCapacityQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
    public void Remove_RandomOrderCapacityComparatorQueueMultipleResize_CorrectlyDequeuesItem() throws InterruptedException {
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
