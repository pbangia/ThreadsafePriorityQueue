package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by priyankitbangia on 5/06/17.
 */
public class BenchmarkTest_Parallel {
    private PipelinedPriorityQueue<Integer> pipelinedQueue;
    private PriorityBlockingQueue<Integer> blockingQueue;
    private int[] threadCases = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
    private int[] inputSizes = new int[]{10,100,1000,10_000,100_000,
            200_000,300_000,400_000,500_000,600_000,700_000,800_000,900_000};

    @Before
    public void before() {
        blockingQueue = new PriorityBlockingQueue<>();
        pipelinedQueue = new PipelinedPriorityQueue<>(100_000);

    }
    //private int[] inputSizes = new int[]{10,100};

    @Test
    public void Put_threadsRandom_BlockingTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads : threadCases) {
                long time = runThreads(numThreads, blockingQueue, numOperations, OperationType.PUT_RANDOM);

                System.out.println("BlockingQueue - put random\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void Put_threadsOrdered_BlockingTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases) {
                long time = runThreads(numThreads, blockingQueue, numOperations, OperationType.PUT_ORDERED);

                System.out.println("BlockingQueue - put ordered\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void Put_threadsReversed_BlockingTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases) {
                long time = runThreads(numThreads, blockingQueue, numOperations, OperationType.PUT_REVERSED);

                System.out.println("BlockingQueue - put reversed\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void MixedOperations_threadsRandom_BlockingTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases) {
                long time = runThreads(numThreads, blockingQueue, numOperations, OperationType.MIXED);

                System.out.println("BlockingQueue - mixed operations\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void Put_threadsRandom_PipelinedTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases){
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.PUT_RANDOM);

                System.out.println("PipelinedPriorityQueue - put random\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void Put_threadsOrdered_PipelinedTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.PUT_ORDERED);

                System.out.println("PipelinedPriorityQueue - put ordered\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void Put_threadsReversed_PipelinedTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.PUT_REVERSED);

                System.out.println("PipelinedPriorityQueue - put reversed\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    @Test
    public void MixedOperations_threadsRandom_PipelinedTiming(){

        for (int numOperations: inputSizes) {
            for (int numThreads: threadCases) {
                long time = runThreads(numThreads, pipelinedQueue, numOperations, OperationType.MIXED);

                System.out.println("PipelinedPriorityQueue - mixed operations\t\t\t" + time
                        + "\t" + numOperations
                        + "\t" + numThreads);
            }
        }
    }

    private long runThreads(int numThreads, BlockingQueue queue, int numOperations, OperationType operation){
        ArrayList<Thread> threads = new ArrayList<>();
        int rangeStart = operation==OperationType.PUT_REVERSED ? numOperations:0;
        int threadSize = numOperations/numThreads;
        for (int i=0; i<numThreads; i++){

            Thread t = null;
            switch (operation){
                case PUT_RANDOM: t = getPutThreadRandom(queue, threadSize); break;
                case PUT_ORDERED: t = getPutThreadOrdered(queue, rangeStart, threadSize);
                    rangeStart+=threadSize;
                    break;
                case PUT_REVERSED: t = getPutThreadReversed(queue, rangeStart, threadSize);
                    rangeStart-=threadSize;
                    break;
                case MIXED: t = getMixedOperationThread(queue, threadSize); break;
                case POLL: t = getPollThread(queue, threadSize);
            }
            t.setName(""+i);
            threads.add(t);
        }

        long start = System.currentTimeMillis();

        for (Thread t: threads) t.start();
        for (Thread t: threads) try{ t.join(); }catch (InterruptedException ex){}

        //System.out.println("all threads finish");
        long end = System.currentTimeMillis();
        blockingQueue.clear();
        pipelinedQueue.clear();
        queue.clear();
        return end-start;
    }

    private Thread getPutThreadRandom(BlockingQueue<Integer> queue, int size) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    try {
                        queue.put(getRandInt());
                    } catch (Exception e) {}
                    dowork();
                }
            }
            private int dowork(){
                int count = 0;
                for (int i = 0; i<getRandInt(); i++){
                    count++;
                }
                return count;
               // if (getRandInt() % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
        });
    }

    private Thread getMixedOperationThread(BlockingQueue<Integer> queue, int size){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<size; i++){
                    if (i%3==0) {
                    } else {
                        int a = getRandInt();

                        try {
                            queue.put(a);
                        } catch (Exception e) {}
                        dowork();
                    }

                }

            }
            private int dowork(){
                int count = 0;
                for (int i = 0; i<getRandInt(); i++){
                    count++;
                }
                return count;
                // if (getRandInt() % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
            //private void sleep(int i){
               // if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            //}
        });
    }

    private Thread getPollThread(BlockingQueue<Integer> queue, int size){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<size; i++){
                    queue.poll();
                    dowork();
                }
            }
            private int dowork(){
                int count = 0;
                for (int i = 0; i<getRandInt(); i++){
                    count++;
                }
                return count;
                // if (getRandInt() % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
            //private void sleep(int i){
               // if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            //}
        });
    }

    private Thread getPutThreadOrdered(BlockingQueue<Integer> queue, int start, int threadSize){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = start; i < start+threadSize; i++) {
                    try {
                        queue.put(i);
                    } catch (Exception e) {}
                    dowork();
                }
            }
            private int dowork(){
                int count = 0;
                for (int i = 0; i<getRandInt(); i++){
                    count++;
                }
                return count;
                // if (getRandInt() % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
            //private void sleep(int i){
                //if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            //}
        });
    }

    private Thread getPutThreadReversed(BlockingQueue<Integer> queue, int start, int threadSize){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = start; i>start-threadSize; i--) {
                    try {
                        queue.put(i);
                    } catch (Exception e) {}
                    dowork();
                }
            }
            private int dowork(){
                int count = 0;
                for (int i = 0; i<getRandInt(); i++){
                    count++;
                }
                return count;
                // if (getRandInt() % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
            //private void sleep(int i){
                //if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            //}
        });
    }

    private int getRandInt() {
        return (int )(Math.random() * 500000 + 1);
    }

    //private enum QueueType {BLOCKING, PIPELINED_DEFAULT};
    private enum OperationType {
        PUT_RANDOM, PUT_ORDERED, PUT_REVERSED, MIXED, POLL
    }

}