package threadsafepq;

/**
 * Created by Taranpreet on 26/05/2017.
 */
public class BinaryTreeUtils {

    public static int convertSizeToNumLevels(int size) {
        return (int) (Math.ceil(Math.log(size + 1) / Math.log(2)));
    }

    public static int convertNumLevelsToSize(int levels) {
        return (int) (Math.pow(2, levels) - 1);
    }

    public static int findLevel(int index) {
        if (index <= 0) return 1;
        return (int) (Math.log(index) / Math.log(2) + 1);
    }

}
