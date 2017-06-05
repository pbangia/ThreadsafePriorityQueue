package threadsafepq;

import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class BenchmarkTest {

    private PipelinedPriorityQueue<Integer> pipelinedQueue;
    private PriorityBlockingQueue<Integer> blockingQ;
    private enum OperationType {PUT_RANDOM,PUT_ORDERED,PUT_REVERSED,MIXED,POLL}
    private enum QueueType {BLOCKING, PIPELINED_DEFAULT, PIPELINED_CAPACITY}
    private int[] inputSizes = new int[]{};

    static final int NUMBER_OF_TESTS = 10;

    @Before
    public void before() {
        pipelinedQueue = new PipelinedPriorityQueue<Integer>();
        blockingQ = new PriorityBlockingQueue<Integer>();
        PriorityBlockingQueue<Integer> q = new PriorityBlockingQueue<Integer>(100001);
        for (int i=0; i<100000; i++) q.put(i);

    }

    @Test
    public void Put_InputSize10000DefaultPipelinedQueuePutOrdered_TimedExecution() throws InterruptedException{
        int inputSize = 10000;
        QueueType queueType = QueueType.PIPELINED_DEFAULT;
        OperationType operationType = OperationType.PUT_ORDERED;

        runSequentialTest(inputSize, queueType, operationType);
    }

    private void runSequentialTest(int inputSize, QueueType queueType, OperationType operationType) throws InterruptedException{
        BlockingQueue<Integer> queue;
        long total = 0;
        int[] inputList = generateInputListFromType(inputSize, operationType);

        switch(queueType) {
            case BLOCKING:
                queue = blockingQ;
                break;
            case PIPELINED_DEFAULT:
                queue = pipelinedQueue;
                break;
            default:
                queue = new PipelinedPriorityQueue<>(inputSize);
                break;
        }

        for (int j = 0; j < NUMBER_OF_TESTS; j++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < inputSize; i++) {
                queue.put(inputList[i]);
            }
            long end = System.currentTimeMillis();
            queue.clear();
            total += (end - start);
        }
        System.out.println("Input Size: " + inputSize +
                ", Queue Type: " + queueType +
                ", Operation Time: " + operationType +
                ", Execution time: " + total/NUMBER_OF_TESTS);

    }

    private int[] generateInputListFromType(int inputSize, OperationType type) {
        int[] list = new int[inputSize];

        for (int i = 0; i < list.length; i++) {
            list[i] = (type == OperationType.PUT_ORDERED) ? i : list.length - i;
        }

        if (type == OperationType.PUT_RANDOM) shuffleArray(list);

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

}