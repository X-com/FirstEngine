import render.SVO;

import java.util.Random;

public class SVOTest {
    public static void main(String[] args) {
        SVO svo = new SVO(3);
        Random random = new Random(4);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    svo.put(i, j, k, random.nextInt());
                }
            }
        }

        random = new Random(4);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if(svo.get(i, j, k) != random.nextInt()){
                        System.out.println("stinky poopy");
                    }
                }
            }
        }
    }
}
