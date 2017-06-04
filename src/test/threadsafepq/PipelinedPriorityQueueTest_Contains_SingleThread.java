package threadsafepq;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taranpreet on 3/06/2017.
 */
public class PipelinedPriorityQueueTest_Contains_SingleThread extends PipelinedPriorityQueueTest {

    @Test
    public void Contains_DefaultQueueEmpty_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        assertEquals(false, queue.contains(0));
    }

    @Test
    public void Contains_DefaultQueueSingleItemWhichExists_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(0);
        assertEquals(true, queue.contains(0));
    }

    @Test
    public void Contains_DefaultQueueSingleItemWhichDoesNotExist_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(0);
        assertEquals(false, queue.contains(1));
    }

    @Test
    public void Contains_DefaultQueueMultipleItemsWhichExist_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(0);
        queue.put(1);
        queue.put(3);
        assertEquals(true, queue.contains(0));
        assertEquals(true, queue.contains(1));
        assertEquals(true, queue.contains(3));
    }

    @Test
    public void Contains_DefaultQueueMultipleItemsWhichDoNotExist_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        queue.put(0);
        queue.put(1);
        queue.put(3);
        assertEquals(false, queue.contains(4));
        assertEquals(false, queue.contains(5));
    }

    @Test
    public void Contains_CapacityQueueEmpty_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        assertEquals(false, queue.contains(0));
    }

    @Test
    public void Contains_CapacityQueueSingleItemWhichExists_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(0);
        assertEquals(true, queue.contains(0));
    }

    @Test
    public void Contains_CapacityQueueSingleItemWhichDoesNotExist_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(0);
        assertEquals(false, queue.contains(1));
    }

    @Test
    public void Contains_CapacityQueueMultipleItemsWhichExist_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(0);
        queue.put(1);
        queue.put(3);
        assertEquals(true, queue.contains(0));
        assertEquals(true, queue.contains(1));
        assertEquals(true, queue.contains(3));
    }

    @Test
    public void Contains_CapacityQueueMultipleItemsWhichDoNotExist_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityQueue;
        queue.put(0);
        queue.put(1);
        queue.put(3);
        assertEquals(false, queue.contains(4));
        assertEquals(false, queue.contains(5));
    }

    @Test
    public void Contains_CapacityComparatorQueueEmpty_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        assertEquals(false, queue.contains(0));
    }

    @Test
    public void Contains_CapacityComparatorQueueSingleItemWhichExists_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(0);
        assertEquals(true, queue.contains(0));
    }

    @Test
    public void Contains_CapacityComparatorQueueSingleItemWhichDoesNotExist_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(0);
        assertEquals(false, queue.contains(1));
    }

    @Test
    public void Contains_CapacityComparatorQueueMultipleItemsWhichExist_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(0);
        queue.put(1);
        queue.put(3);
        assertEquals(true, queue.contains(0));
        assertEquals(true, queue.contains(1));
        assertEquals(true, queue.contains(3));
    }

    @Test
    public void Contains_CapacityComparatorQueueMultipleItemsWhichDoNotExist_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = capacityComparatorQueue;
        queue.put(0);
        queue.put(1);
        queue.put(3);
        assertEquals(false, queue.contains(4));
        assertEquals(false, queue.contains(5));
    }

    @Test
    public void ContainsAll_DefaultQueueEmpty_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        List<Integer> list = new ArrayList<>();
        list.add(1);
        assertEquals(false, queue.containsAll(list));
    }

    @Test
    public void ContainsAll_DefaultQueueWithAllItemsExisting_ReturnsTrue() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
            queue.put(i);
        }

        assertEquals(true, queue.containsAll(list));
    }

    @Test
    public void ContainsAll_DefaultQueueWithSomeItemsMissing_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
            queue.put(i);
        }

        queue.remove();
        queue.remove();
        queue.remove();

        assertEquals(false, queue.containsAll(list));
    }

    @Test
    public void ContainsAll_DefaultQueueWithOneItemMissing_ReturnsFalse() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
            queue.put(i);
        }

        queue.remove();

        assertEquals(false, queue.containsAll(list));
    }

    @Test(expected = NullPointerException.class)
    public void Contains_DefaultQueueWithNullElements_ThrowsNullPointerException() {
        PipelinedPriorityQueue<Integer> queue = defaultQueue;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
            queue.put(i);
        }

        list.set(5, null);

        queue.containsAll(list);
    }
}
