package model;

import opengl.Texture;
import opengl.VertexArray;

public class Model {

    private VertexArray vao;
    private Texture tex;

    public Model(VertexArray vao, Texture tex) {
        this.vao = vao;
        this.tex = tex;
    }

    public int getVertexCount() {
        return vao.getIndexBufferSize();
    }

    public void bind() {
        vao.bind();
        tex.bind();
    }

    public void unbind() {
        vao.unbind();
        tex.unbind();
    }
}
