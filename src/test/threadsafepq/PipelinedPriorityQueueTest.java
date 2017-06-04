package threadsafepq;

import org.junit.Before;

public abstract class PipelinedPriorityQueueTest {

    static final int DEFAULT_INITIAL_CAPACITY = 11;
    static final int CUSTOM_INITIAL_CAPACITY = 42;
    PipelinedPriorityQueue<Integer> defaultQueue;
    PipelinedPriorityQueue<Integer> capacityQueue;
    PipelinedPriorityQueue<Integer> capacityComparatorQueue;

    @Before
    public void before() {
        defaultQueue = new PipelinedPriorityQueue<Integer>();
        capacityQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY);
        capacityComparatorQueue = new PipelinedPriorityQueue<>(CUSTOM_INITIAL_CAPACITY, (o1, o2) -> o1.compareTo(o2));
    }

}
