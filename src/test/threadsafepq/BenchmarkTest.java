package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BenchmarkTest {

    private PipelinedPriorityQueue<Integer> pipelinedQ;
    private PriorityBlockingQueue<Integer> blockingQ;

    @Before
    public void before() {
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



}