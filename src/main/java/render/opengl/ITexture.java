package render.opengl;

public interface ITexture {
    void bind();
    void bind(int slot);
    void unbind();
}
