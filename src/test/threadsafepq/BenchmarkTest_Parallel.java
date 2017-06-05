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
    @Before
    public void before() {
        blockingQueue = new PriorityBlockingQueue<>();
        queue = new PipelinedPriorityQueue<>();

    }

    @Test
    public void Put_7threadsRandom_BlockingTiming(){
        int numOperations = 150_000;
        int numThreads = 7;
        long time = runThreads(7, QueueType.BLOCKING, numOperations);
        System.out.println("BlockingQueue\t\t\t{ time: "+time
                +", Number of put operations: " + numOperations
                +", Threads: "+numThreads + "}");

    }

    @Test
    public void Put_7threadsRandom_PipelinedTiming(){
        queue = new PipelinedPriorityQueue<>(150_000);
        int numOperations = 150_000;
        int numThreads = 7;
        long time = runThreads(numThreads, QueueType.PIPELINED, numOperations);
        System.out.println("PipelinedPriorityQueue\t{ time: "+time
                +", Number of put operations: " + numOperations
                +", Threads: "+numThreads+"}");

    }

    private long runThreads(int numThreads, QueueType type, int numOperations){

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i=0; i<numThreads; i++){
            int threadSize = numOperations/numThreads;
            Thread t = getPutThreadRandom(type, threadSize);
            threads.add(t);
        }

        long start = System.currentTimeMillis();

        for (Thread t: threads) t.start();
        for (Thread t: threads) try{ t.join(); }catch (InterruptedException ex){}

        long end = System.currentTimeMillis();
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

    private int getRandInt() {
        return (int )(Math.random() * 5000 + 1);
    }

}