import render.SVO;

import java.util.Random;

public class SVOTest {
    public static void main(String[] args) {
        int n = 9;
        int s = 1<<n;
        SVO svo = new SVO(n);

        Random random = new Random(4);

        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < s; k++) {
                    svo.put(i, j, k, random.nextInt());
                }
            }
        }

        for (int i = 0; i < s/2; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < s; k++) {
                    svo.put(i, j, k, 0);
                }
            }
        }
        long lastTime = System.currentTimeMillis();
        svo.clean();
        System.out.println(System.currentTimeMillis()-lastTime);


        random = new Random(4);
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < s; k++) {
                    int next = random.nextInt();
                    if(i >= s/2 && svo.get(i, j, k) != next){
                        System.out.println("uhoh stinky");
                    }
                }
            }
        }

    }
}
