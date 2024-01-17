package file;

public class IntAccum {

    private int[] data;
    private int len = 0;
    public IntAccum(){
        data = new int[8];
    }

    public int size(){
        return len;
    }

    public void add(int f){
        if(len==data.length){
            upsize();
        }
        data[len] = f;
        len++;
    }

    private void upsize(){
        int[] newData = new int[data.length*2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    public void add(int[] ar, int start, int count){
        while (len+count>=data.length){
            upsize();
        }
        System.arraycopy(ar, start, data, len, count);
        len += count;
    }

    public void add(int... ar){
        add(ar, 0, ar.length);
    }

    public int[] getData() {
        int[] copy = new int[len];
        System.arraycopy(data, 0, copy, 0, len);
        return copy;
    }

    public void clear(){
        len = 0;
    }

    public boolean empty(){
        return len==0;
    }
}
