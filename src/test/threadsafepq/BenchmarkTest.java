package threadsafepq;

import jdk.internal.util.xml.impl.Input;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BenchmarkTest {

    private PipelinedPriorityQueue<Integer> defaultQueue;
    private PipelinedPriorityQueue<Integer> pipelinedQ;
    private PriorityBlockingQueue<Integer> blockingQ;

    static final int NUMBER_OF_TESTS = 10;

    @Before
    public void before() {
        defaultQueue = new PipelinedPriorityQueue<Integer>();
        blockingQ = new PriorityBlockingQueue<Integer>();
        pipelinedQ = new PipelinedPriorityQueue<Integer>(1000001);
        PriorityBlockingQueue<Integer> q = new PriorityBlockingQueue<Integer>(100001);
        for (int i=0; i<100000; i++) q.put(i);

    }

    @Test
    public void Put_BlockingQueueTiming(){
        long total=0;
        for (int j=0; j<10; j++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                blockingQ.put(i);
            }
            long end = System.currentTimeMillis();
            blockingQ.clear();
            long elapsed = (end-start);
            total+=elapsed;
        }

        System.out.println("PriorityBlockingQueue time:" + total/10);
    }

    @Test
    public void Put_PipelinedTiming() {
        long total=0;
        for (int j=0; j<1; j++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                pipelinedQ.put(i);
            }
            long end = System.currentTimeMillis();
            pipelinedQ.clear();
            long elapsed = (end-start);
            total+=elapsed;
        }
        System.out.println("PipelinedPriorityQueue time:" + total/10);

    }

    @Test
    public void Put_InputSize10000DefaultQueueInOrder_TimedExecution() {
        int inputSize = 10000;
        boolean isDefaultQueue = true;
        InputType type = InputType.IN_ORDER;

        sequential_Test(inputSize, isDefaultQueue, type);
    }

    private void sequential_Test(int inputSize, boolean isDefaultQueue, InputType type) {
        long total = 0;
        PipelinedPriorityQueue<Integer> queue = (isDefaultQueue) ? defaultQueue : new PipelinedPriorityQueue<>(inputSize);
        int[] inputList = generateInputListFromType(inputSize, type);

        for (int j = 0; j < NUMBER_OF_TESTS; j++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < inputSize; i++) {
                queue.put(inputList[i]);
            }
            long end = System.currentTimeMillis();
            defaultQueue.clear();
            total += (end - start);
        }
        System.out.println("Input Size: " + inputSize +
                ", Default Queue: " + isDefaultQueue +
                ", Input Type: " + type +
                ", Execution time: " + total/NUMBER_OF_TESTS);
    }

    private int[] generateInputListFromType(int inputSize, InputType type) {
        int[] list = new int[inputSize];

        for (int i = 0; i < list.length; i++) {
            list[i] = (type == InputType.IN_ORDER) ? i : list.length - i;
        }

        if (type == InputType.SHUFFLE) shuffleArray(list);

        return list;
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

    protected enum InputType {
        IN_ORDER,
        REVERSE_ORDER,
        SHUFFLE
    }

}