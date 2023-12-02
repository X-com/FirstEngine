package file;

public class FloatAccum {
    private float[] data;
    private int len = 0;
    public FloatAccum(){
        data = new float[8];
    }

    public int size(){
        return len;
    }

    public void add(float f){
        if(len==data.length){
            upsize();
        }
        data[len] = f;
        len++;
    }

    public void add(float[] ar, int start, int count){
        if(len+count>=data.length){
            upsize();
        }
        System.arraycopy(ar, start, data, len, count);
        len += count;
    }

    private void upsize(){
        float[] newData = new float[data.length*2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    public float[] getData() {
        float[] copy = new float[len];
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
