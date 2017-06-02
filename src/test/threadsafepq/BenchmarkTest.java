package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Taranpreet on 27/05/2017.
 */
public class BenchmarkTest {

    private PipelinedPriorityQueue<Integer> pipelinedQ;
    private PriorityBlockingQueue<Integer> blockingQ;

    @Before
    public void before() {
        blockingQ = new PriorityBlockingQueue<Integer>(100001);
        pipelinedQ = new PipelinedPriorityQueue<Integer>(100001);
    }

    @Test
    public void Put_DefaultEmptyQueues_Timing() {

        long start = System.currentTimeMillis();
        for (int i=0; i<100000; i++){
            blockingQ.put(i);
        }
        long end = System.currentTimeMillis();
        long elapsed = end-start;
        System.out.println("PriorityBlockingQueue time:" + elapsed);

        start = System.currentTimeMillis();
        for (int i=0; i<100000; i++){
            pipelinedQ.put(i);
        }
        end = System.currentTimeMillis();
        elapsed = end-start;
        System.out.println("PipelinedPriorityQueue time:" + elapsed);
    }


}