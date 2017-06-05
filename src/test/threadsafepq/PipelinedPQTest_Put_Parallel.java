package threadsafepq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.PriorityBlockingQueue;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PipelinedPQTest_Put_Parallel {

    private PipelinedPriorityQueue<Integer> queue;
    private PriorityBlockingQueue<Integer> blockingQueue;
    private enum QueueType {BLOCKING, PIPELINED};
    @Before
    public void before() {
        blockingQueue = new PriorityBlockingQueue<>();
        queue = new PipelinedPriorityQueue<>();
    }

    @Test
    public void Put_Parallel_Correct(){
        //t1 puts 1-5
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread 1: put "+10);
                queue.put(10);

                try { Thread.sleep(1); } catch (Exception e) {}
                System.out.println("Thread 1: put "+20);
                queue.put(20);

                System.out.println("Thread 1: put "+3);
                queue.put(3);

                try { Thread.sleep(1); } catch (Exception e) {}
                System.out.println("Thread 1: put "+17);
                queue.put(17);

                System.out.println("Thread 1: put "+14);
                queue.put(14);

                System.out.println("Thread 1: put "+6);
                queue.put(6);
                System.out.println("1 finished");

            }
        });

        //t2 puts 6-10
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(2); } catch (Exception e) {}
                System.out.println("Thread 2: put "+19);
                queue.put(19);

                System.out.println("Thread 2: put "+5);
                queue.put(5);

                System.out.println("Thread 2: put "+11);
                queue.put(11);

                try { Thread.sleep(1); } catch (Exception e) {}
                System.out.println("Thread 2: put "+8);
                queue.put(8);

                System.out.println("Thread 2: put "+16);
                queue.put(16);
                System.out.println("2 finished");
            }
        });

        //t2 puts 6-10
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread 3: put "+9);
                queue.put(9);

                try { Thread.sleep(1); } catch (Exception e) {}
                System.out.println("Thread 3: put "+15);
                queue.put(15);

                System.out.println("Thread 3: put "+1);
                queue.put(1);

                try { Thread.sleep(1); } catch (Exception e) {}
                System.out.println("Thread 3: put "+18);
                queue.put(18);

                System.out.println("Thread 3: put "+2);
                queue.put(2);
                System.out.println("3 finished");
            }
        });

        //t2 puts 6-10
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread 4: put "+0);
                queue.put(0);

                try { Thread.sleep(1); } catch (Exception e) {}
                System.out.println("Thread 4: put "+13);
                queue.put(13);

                System.out.println("Thread 4: put "+7);
                queue.put(7);

                System.out.println("Thread 4: put "+12);
                queue.put(12);

                System.out.println("Thread 4: put "+4);
                queue.put(4);

                System.out.println("4 finished");
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException ex){}

        for (int i = 0; i<21; i++){
            int removed = queue.poll();
            assertEquals("Poll("+i+"):\n"+queue.toString(),i, removed);
        }

    }

    @Test
    private void Put_Parallel2_Correct(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
               queue.put(1);
            }
        });
    }



}