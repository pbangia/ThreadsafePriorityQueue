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
        blockingQ = new PriorityBlockingQueue<Integer>();
        pipelinedQ = new PipelinedPriorityQueue<Integer>();
    }

}