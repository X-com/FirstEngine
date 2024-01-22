package render;

import java.nio.ByteBuffer;

public class SVO {
    private static final int MASK = Integer.MIN_VALUE;
    private int[] ar;
    private int end;//index of the element following the end of the svo in the array
    private final int levels;
    public SVO(int levels){
        this.levels = levels;
        end = 8;
        ar = new int[8];
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
    }

    public int get(int x, int y, int z){
        x = x<<(32-levels);
        y = y<<(32-levels);
        z = z<<(32-levels);

        int currPtr = 0;

        for (int i = 0; i < levels; i++) {
            int il = (x&MASK)>>>31 | (y&MASK)>>>30 | (z&MASK)>>>29;
            int newPtr = ar[currPtr+il];
            if(newPtr==0){
                return 0;
            }
            currPtr = newPtr;
            x <<= 1;
            y <<= 1;
            z <<= 1;
        }
        return currPtr;
    }
    public int put(int x, int y, int z, int id){
        x = x<<(32-levels);
        y = y<<(32-levels);
        z = z<<(32-levels);

        int currPtr = 0;
        for (int i = 0; i < levels-1; i++) {
            int il = (x&MASK)>>>31 | (y&MASK)>>>30 | (z&MASK)>>>29;
            int newPtr = ar[currPtr+il];
            if(newPtr==0){
                ar[currPtr+il] = newPtr = end;
                end += 8;
                ensureCapacity();
            }
            currPtr = newPtr;
            x <<= 1;
            y <<= 1;
            z <<= 1;
        }
        int il = (x&MASK)>>>31 | (y&MASK)>>>30 | (z&MASK)>>>29;

        currPtr += il;

        int oldId = ar[currPtr];
        ar[currPtr] = id;
        return oldId;
    }

    public void clean(){
        int[] newAr = new int[ar.length];
        end = clean(newAr, 0, levels-1, 0);
        ar = newAr;
    }

    private int clean(int[] newAr, int newPtr, int level, int oldPtr){
        int end = newPtr+8;

        if(level==0){
            boolean trim = true;
            for (int i = 0; i < 8; i++) {
                int ptr = newAr[newPtr+i] = ar[oldPtr+i];
                trim &= (ptr == 0);
            }
            if(trim) return newPtr;
            return end;
        }

        for (int i = 0; i < 8; i++) {
            int ptr = ar[oldPtr+i];
            if (ptr != 0) {
                newAr[newPtr+i] = end;
                end = clean(newAr, end, level-1, ptr);
            }
        }

        if(end==newPtr+8) return newPtr;
        return end;
    }

    private void ensureCapacity(){
        int newLen = ar.length;
        while (end >= newLen){
            newLen <<= 1;
        }
        if (newLen > ar.length) {
            int[] newAr = new int[newLen];
            System.arraycopy(ar, 0, newAr, 0, ar.length);
            ar = newAr;
        }
    }
}
