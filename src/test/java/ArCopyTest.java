import java.util.Collections;
import java.util.Random;

public class ArCopyTest {
    public static void main(String[] args) {
        int[] test = new int[100000000];
        for (int i = 0; i < test.length; i++) {
            test[i] = i;
        }
        shuffleArray(test);
        int[] test2 = new int[test.length];

        long lastTime = System.currentTimeMillis();

        System.arraycopy(test, 0, test2, 0, test.length);
        System.out.println(System.currentTimeMillis()-lastTime);

        lastTime = System.currentTimeMillis();
        int ptr = 0;
        for (int i = 0; i < test.length; i++) {
            test2[ptr] = test[ptr];
            ptr = test[ptr];
        }

        System.out.println(System.currentTimeMillis()-lastTime);


    }

    private static void shuffleArray(int[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
}
