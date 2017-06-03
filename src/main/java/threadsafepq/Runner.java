package threadsafepq;

import java.util.Arrays;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class Runner {

    public static void main(String[] args) {
        PipelinedPriorityQueue<Integer> p = new PipelinedPriorityQueue<Integer>();

        int[] array = {1, 3, 5, 6, 7};
        int[] oldArray = array;
        array = new int[5];

        System.out.println(Arrays.toString(oldArray));
        System.out.println(Arrays.toString(array));
    }
}
