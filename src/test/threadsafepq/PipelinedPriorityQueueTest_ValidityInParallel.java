package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.fail;

public class PipelinedPriorityQueueTest_ValidityInParallel {

    private PipelinedPriorityQueue<Integer> pipelinedQueue;
    private int[] threadCases = new int[]{1, 2, 4, 6, 8, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    private int[] inputSizes = new int[]{10, 100, 1000, 10_000, 100_000};
//            200_000, 300_000, 400_000, 500_000, 600_000, 700_000, 800_000, 900_000};

    @Before
    public void before() {
        pipelinedQueue = new PipelinedPriorityQueue<>();
    }

    private void confirmValidity(BlockingQueue<Integer> blockingQueue) {

        Object[] elements = blockingQueue.toArray();
        Arrays.sort(elements);

        Integer last = (Integer) elements[0];
        for (int i = 1; i < elements.length; i++) {
            Integer current = (Integer) elements[i];
            if (current.compareTo(last) < 0) {
                fail();
            }
        }

    }

    @Test
    public void Put_threadsRandom_PipelinedTiming() {

        for (int numOperations : inputSizes) {
            for (int numThreads : threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.PUT_RANDOM);
                confirmValidity(pipelinedQueue);
            }
        }
    }

    @Test
    public void Put_threadsOrdered_PipelinedTiming() {

        for (int numOperations : inputSizes) {
            for (int numThreads : threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.PUT_ORDERED);
                confirmValidity(pipelinedQueue);
            }
        }
    }

    @Test
    public void Put_threadsReversed_PipelinedTiming() {

        for (int numOperations : inputSizes) {
            for (int numThreads : threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.PUT_REVERSED);
                confirmValidity(pipelinedQueue);
            }
        }
    }

    @Test
    public void MixedOperations_threadsRandom_PipelinedTiming() {

        for (int numOperations : inputSizes) {
            for (int numThreads : threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.MIXED);
                confirmValidity(pipelinedQueue);
            }
        }
    }

    private long runThreads(int numThreads, BlockingQueue queue, int numOperations, OperationType operation) {
        List<Thread> threads = new ArrayList<>();

        int rangeStart = operation == OperationType.PUT_REVERSED
                ? numOperations
                : 0;

        int threadSize = numOperations / numThreads;
        for (int i = 0; i < numThreads; i++) {
            Thread t = null;
            switch (operation) {
                case PUT_RANDOM:
                    t = getPutThreadRandom(queue, threadSize);
                    break;
                case PUT_ORDERED:
                    t = getPutThreadOrdered(queue, rangeStart, threadSize);
                    rangeStart += threadSize;
                    break;
                case PUT_REVERSED:
                    t = getPutThreadReversed(queue, rangeStart, threadSize);
                    rangeStart -= threadSize;
                    break;
                case MIXED:
                    t = getMixedOperationThread(queue, threadSize);
                    break;
                case POLL:
                    t = getPollThread(queue, threadSize);
                    break;
            }
            t.setName("" + i);
            threads.add(t);
        }

        long start = System.currentTimeMillis();

        for (Thread t : threads) t.start();
        for (Thread t : threads)
            try {
                t.join();
            } catch (InterruptedException ignored) {

            }


        long end = System.currentTimeMillis();
        return end - start;
    }

    private Thread getPutThreadRandom(BlockingQueue<Integer> queue, int size) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    try {
                        queue.put(getRandInt());
                    } catch (Exception ignored) {
                    }
                    sleep(i);
                }
            }

            private void sleep(int i) {
                if (i % 3 == 0) {
                    try {
                        Thread.sleep(0);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private Thread getMixedOperationThread(BlockingQueue<Integer> queue, int size) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    if (i % 3 == 0) {
                    } else {
                        int a = getRandInt();
                        try {
                            queue.put(a);
                        } catch (Exception e) {
                        }
                        sleep(i);
                    }
                }
            }

            private void sleep(int i) {
                if (i % 3 == 0) {
                    try {
                        Thread.sleep(0);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private Thread getPollThread(BlockingQueue<Integer> queue, int size) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    queue.poll();
                    sleep(i);
                }
            }

            private void sleep(int i) {
                if (i % 3 == 0) {
                    try {
                        Thread.sleep(0);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private Thread getPutThreadOrdered(BlockingQueue<Integer> queue, int start, int threadSize) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = start; i < start + threadSize; i++) {
                    try {
                        queue.put(i);
                    } catch (Exception e) {
                    }
                    sleep(i);
                }
            }

            private void sleep(int i) {
                if (i % 3 == 0) {
                    try {
                        Thread.sleep(0);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private Thread getPutThreadReversed(BlockingQueue<Integer> queue, int start, int threadSize) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = start; i > start - threadSize; i--) {
                    try {
                        queue.put(i);
                    } catch (Exception e) {
                    }
                    sleep(i);
                }
            }

            private void sleep(int i) {
                if (i % 3 == 0) {
                    try {
                        Thread.sleep(0);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private int getRandInt() {
        return (int) (Math.random() * 500000 + 1);
    }

    private enum OperationType {PUT_RANDOM, PUT_ORDERED, PUT_REVERSED, MIXED, POLL}

}
