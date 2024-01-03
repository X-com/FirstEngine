package opengl;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class VertexArray {
    private int vao;
    private ArrayList<VertexBuffer> vbos;
    private IndexBuffer indexBuffer;
    private int indexBufferSize;

    public VertexArray() {
        vao = glGenVertexArrays();
        vbos = new ArrayList<>();
        glBindVertexArray(vao);
    }

    public VertexArray addVertexBuffer(VertexBuffer vbo, VertexBufferLayout layout) {
        vbos.add(vbo);
        bind();
        vbo.bind();
        int offset = 0;
        for (int i = 0; i < layout.size(); i++) {
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, layout.getCount(i), layout.getType(i), layout.getNormalized(i), layout.getStride(), offset);
            offset += layout.getOffset(i);
        }
        return this;
    }

    public VertexArray addVertexBuffer(VertexBuffer vbo) {
        vbos.add(vbo);
        bind();
        vbo.bind();
        glVertexAttribPointer(vbo.getAttributeNumber(), vbo.getSize(), GL11.GL_FLOAT, vbo.getDynamic(), 0, 0);
        vbo.unbind();
        return this;
    }

//    public void updateDynamicVertexBufferFloats() {
//        vbos.updateDynamicVertices();
//    }
//
//    public float[] getDynamicVertexBufferFloats() {
//        return vbos.getDynamicVertices();
//    }

    public void setIndexBuffer(IndexBuffer ib) {
        this.indexBuffer = ib;
        this.indexBufferSize = ib.getCount();
    }

    public int getIndexBufferSize() {
        return indexBufferSize;
    }

    public void bind() {
        glBindVertexArray(vao);
        for (VertexBuffer vbo : vbos) {
            vbo.bindAttribute();
        }
    }

    public void unbind() {
        for (VertexBuffer vbo : vbos) {
            vbo.unbindAttribute();
        }
        glBindVertexArray(0);
    }

    public void dispose() {
        for (VertexBuffer vbo : vbos) {
            vbo.dispose();
        }
        glDeleteVertexArrays(vao);
    }
}
