package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by priyankitbangia on 5/06/17.
 */
public class BenchmarkTest_Parallel {
    private PipelinedPriorityQueue<Integer> queue;
    private PriorityBlockingQueue<Integer> blockingQueue;
    private enum QueueType {BLOCKING, PIPELINED};
    private enum OperationType {PUT_RANDOM,PUT_ORDERED,PUT_REVERSED,MIXED,POLL}
    private int[] threadCases = new int[]{8,10};
    @Before
    public void before() {
        blockingQueue = new PriorityBlockingQueue<>();
        queue = new PipelinedPriorityQueue<>();

    }

    @Test
    public void Put_threadsRandom_BlockingTiming(){
        int numOperations = 150_000;

        for (int numThreads: threadCases) {
            long time = runThreads(numThreads, QueueType.BLOCKING, numOperations, OperationType.PUT_RANDOM);

            System.out.println("BlockingQueue - random\t\t\t { time: " + time
                    + ", Number of put operations: " + numOperations
                    + ", Threads: " + numThreads + "}");
        }

    }

    @Test
    public void Put_threadsOrdered_BlockingTiming(){
        int numOperations = 150_000;

        for (int numThreads: threadCases) {
            long time = runThreads(numThreads, QueueType.BLOCKING, numOperations, OperationType.PUT_ORDERED);

            System.out.println("BlockingQueue - ordered\t\t\t { time: " + time
                    + ", Number of put operations: " + numOperations
                    + ", Threads: " + numThreads + "}");
        }
    }

    @Test
    public void Put_threadsReversed_BlockingTiming(){
        int numOperations = 150_000;

        for (int numThreads: threadCases) {
            long time = runThreads(numThreads, QueueType.BLOCKING, numOperations, OperationType.PUT_REVERSED);

            System.out.println("BlockingQueue - reversed\t\t\t { time: " + time
                    + ", Number of put operations: " + numOperations
                    + ", Threads: " + numThreads + "}");
        }
    }

    @Test
    public void Put_threadsRandom_PipelinedTiming(){
        queue = new PipelinedPriorityQueue<>(150_000);
        int numOperations = 150_000;

        for (int numThreads: threadCases){
            long time = runThreads(numThreads, QueueType.PIPELINED, numOperations, OperationType.PUT_RANDOM);

            System.out.println("PipelinedPriorityQueue - random\t { time: "+time
                    +", Number of put operations: " + numOperations
                    +", Threads: "+numThreads+"}");
        }
    }

    @Test
    public void Put_threadsOrdered_PipelinedTiming(){
        int numOperations = 150_000;

        for (int numThreads: threadCases) {
            long time = runThreads(numThreads, QueueType.BLOCKING, numOperations, OperationType.PUT_ORDERED);

            System.out.println("PipelinedPriorityQueue - ordered\t { time: " + time
                    + ", Number of put operations: " + numOperations
                    + ", Threads: " + numThreads + "}");
        }
    }

    @Test
    public void MixedOperations_threadsRandom_PipelinedTiming(){
        int numOperations = 150_000;

        for (int numThreads: threadCases) {
            long time = runThreads(numThreads, QueueType.BLOCKING, numOperations, OperationType.MIXED);

            System.out.println("PipelinedPriorityQueue - mixed operations\t { time: " + time
                    + ", Number of put operations: " + numOperations
                    + ", Threads: " + numThreads + "}");
        }
    }

    @Test
    public void Put_threadsReversed_PipelinedTiming(){
        int numOperations = 150_000;

        for (int numThreads: threadCases) {
            long time = runThreads(numThreads, QueueType.BLOCKING, numOperations, OperationType.PUT_REVERSED);

            System.out.println("PipelinedPriorityQueue - reversed\t { time: " + time
                    + ", Number of put operations: " + numOperations
                    + ", Threads: " + numThreads + "}");
        }
    }

    private long runThreads(int numThreads, QueueType type, int numOperations, OperationType operation){

        ArrayList<Thread> threads = new ArrayList<>();
        int rangeStart = operation==OperationType.PUT_REVERSED ? numOperations:0;
        int threadSize = numOperations/numThreads;
        for (int i=0; i<numThreads; i++){

            Thread t = null;
            switch (operation){
                case PUT_RANDOM: t = getPutThreadRandom(type, threadSize); break;
                case PUT_ORDERED: t = getPutThreadOrdered(type, rangeStart, threadSize);
                    rangeStart+=threadSize;
                    break;
                case PUT_REVERSED: t = getPutThreadReversed(type, rangeStart, threadSize);
                    rangeStart-=threadSize;
                    break;
                case MIXED: t = getMixedOperationThread(type, threadSize); break;
                case POLL: t = getPollThread(type, threadSize);
            }
            threads.add(t);
        }

        long start = System.currentTimeMillis();

        for (Thread t: threads) t.start();
        for (Thread t: threads) try{ t.join(); }catch (InterruptedException ex){}


        long end = System.currentTimeMillis();
        blockingQueue.clear();
        queue.clear();
        return end-start;
    }

    private Thread getPutThreadRandom(QueueType type, int size){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if (type== QueueType.PIPELINED){
                    for (int i=0; i<size; i++){
                        queue.put(getRandInt());
                        sleep(i);
                    }
                    return;
                }
                for (int i = 0; i < size; i++) {
                    blockingQueue.put(getRandInt());
                    sleep(i);
                }
            }
            private void sleep(int i){
                if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
        });
    }

    private Thread getMixedOperationThread(QueueType type, int size){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if (type== QueueType.PIPELINED){
                    for (int i=0; i<size; i++){
                        if (i%2==0) {
                            queue.put(getRandInt());
                        } else queue.poll();
                        sleep(i);
                    }
                    return;
                }
                for (int i = 0; i < size; i++) {
                    if (i%2==0) {
                        blockingQueue.put(getRandInt());
                    } else blockingQueue.poll();
                    sleep(i);
                }
            }
            private void sleep(int i){
                if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
        });
    }

    private Thread getPollThread(QueueType type, int size){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if (type== QueueType.PIPELINED){
                    for (int i=0; i<size; i++){
                        queue.poll();
                        sleep(i);
                    }
                    return;
                }
                for (int i=0; i<size; i++){
                    blockingQueue.poll();
                    sleep(i);
                }
            }
            private void sleep(int i){
                if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
        });
    }

    private Thread getPutThreadOrdered(QueueType type, int start, int threadSize){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if (type== QueueType.PIPELINED){
                    for (int i=start; i<start+threadSize; i++){
                        queue.put(i);
                        sleep(i);
                    }
                    return;
                }
                for (int i = start; i < start+threadSize; i++) {
                    blockingQueue.put(i);
                    sleep(i);
                }
            }
            private void sleep(int i){
                if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
        });
    }

    private Thread getPutThreadReversed(QueueType type, int start, int threadSize){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if (type== QueueType.PIPELINED){
                    for (int i=start; i>start-threadSize; i--){
                        queue.put(i);
                        sleep(i);
                    }
                    return;
                }
                for (int i = start; i>start-threadSize; i--) {
                    blockingQueue.put(i);
                    sleep(i);
                }
            }
            private void sleep(int i){
                if (i % 3 == 0) { try { Thread.sleep(1); } catch (Exception e) {} }
            }
        });
    }

    private int getRandInt() {
        return (int )(Math.random() * 500000 + 1);
    }

}