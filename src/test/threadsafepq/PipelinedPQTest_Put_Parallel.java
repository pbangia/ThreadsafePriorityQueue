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
    @Before
    public void before() {
        queue = new PipelinedPriorityQueue<>(400);
        blockingQueue = new PriorityBlockingQueue<>(400);
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

            }
        });

        //t2 puts 6-10
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(2); } catch (Exception e) {}
                System.out.println("Thread 2: put "+9);
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
    public void Put_Parallel2_Correct(){
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(1); } catch (Exception e) {}
                for (int i=200; i<300; i++){
                    if (i%2==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                    queue.put(i);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=100; i<200; i++){
                    queue.put(i);
                    if (i%2==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=300; i<400; i++){
                    queue.put(i);
                    if (i%2==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                }
            }
        });

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100; i++){
                    queue.put(i);
                    if (i%3==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                }
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
        long end = System.currentTimeMillis();
        long elapsed = (end-start);
        System.out.println("blocking:"+elapsed);
        for (int i = 0; i<400; i++){
            int removed = queue.poll();
            assertEquals("Poll("+i+")",i, removed);
        }

    }

    @Test
    public void Put_Parallel3_Correct(){
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(1); } catch (Exception e) {}
                for (int i=200; i<300; i++){
                    if (i%2==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                    blockingQueue.put(i);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=100; i<200; i++){
                    blockingQueue.put(i);
                    if (i%2==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=300; i<400; i++){
                    blockingQueue.put(i);
                    if (i%2==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                }
            }
        });

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100; i++){
                    blockingQueue.put(i);
                    if (i%3==0) {
                        try { Thread.sleep(1); } catch (Exception e) {}
                    }
                }
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
        long end = System.currentTimeMillis();
        long elapsed = (end-start);
        System.out.println("blocking:"+elapsed);
        for (int i = 0; i<400; i++){
            int removed = blockingQueue.poll();
            assertEquals("Poll("+i+")",i, removed);
        }

    }

}