package opengl;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class VertexBufferLayout {
    private int stride;
    private ArrayList<Attribute> attributes;

    public VertexBufferLayout() {
        attributes = new ArrayList<>();
    }

    public int getStride() {
        return stride;
    }

    public Attribute[] getAttributes() {
        return attributes.toArray(new Attribute[0]);
    }

    public void addFloat(int count) {
        attributes.add(new Attribute(GL_FLOAT, count, false, Float.BYTES));
        stride += count * 4;
    }

    public void addUnsignedInt(int count) {
        attributes.add(new Attribute(GL_UNSIGNED_INT, count, false, Float.BYTES));
        stride += count * 4;
    }

    public void addUnsignedChar(int count) {
        attributes.add(new Attribute(GL_UNSIGNED_BYTE, count, true, Byte.BYTES));
        stride += count;
    }

    public int size() {
        return attributes.size();
    }

    public int getCount(int i) {
        return attributes.get(i).count;
    }

    public int getType(int i) {
        return attributes.get(i).type;
    }

    public boolean getNormalized(int i) {
        return attributes.get(i).normalized;
    }

    public int getOffset(int i) {
        return attributes.get(i).count * attributes.get(i).typeSize;
    }

    @Override
    public String toString() {
        return "VertexBufferLayout{" +
                "\n   stride=" + stride +
                "\n   elements=" + Arrays.toString(getAttributes());
    }

    private static class Attribute {
        private final int type;
        private final int count;
        private final boolean normalized;
        private final int typeSize;

        public Attribute(int type, int count, boolean normalized, int typeSize) {
            this.type = type;
            this.count = count;
            this.normalized = normalized;
            this.typeSize = typeSize;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "\n   count=" + count +
                    "\n   type=" + type +
                    "\n   normalized=" + normalized;
        }
    }
}
