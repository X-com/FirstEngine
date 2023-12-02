package opengl;

import static org.lwjgl.opengl.GL33.*;

public class VertexArray {
    private int vao;
    private IndexBuffer indexBuffer;
    private int attribPointer = 0;

    public VertexArray() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
    }

    public VertexArray addVertexBuffer(VertexBuffer vb, VertexBufferLayout layout) {
        bind();
        vb.bind();
        int offset = 0;
        for (int i = 0; i < layout.size(); i++) {
            glEnableVertexAttribArray(attribPointer+i);
            glVertexAttribPointer(i, layout.getCount(i), layout.getType(i), layout.getNormalized(i), layout.getStride(), offset);
            offset += layout.getOffset(i);
        }
        attribPointer += layout.size();
        return this;
    }

    public void setIndexBuffer(IndexBuffer ib) {
        this.indexBuffer = ib;
    }

    public IndexBuffer getIndexBuffer() {
        return indexBuffer;
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void dispose() {
        glDeleteVertexArrays(vao);
    }
}
