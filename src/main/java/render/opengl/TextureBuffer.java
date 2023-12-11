package render.opengl;

import org.lwjgl.BufferUtils;

import static com.badlogic.gdx.graphics.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.glTexBuffer;

public class TextureBuffer implements ITexture {
    private int textureId;
    public TextureBuffer(float[] data, int format){
        int buffer = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, buffer);
        glBufferData(GL_TEXTURE_BUFFER, data, GL_STATIC_DRAW);
        initTexture(buffer, format);
    }

    public TextureBuffer(int buffer, int format){
        initTexture(buffer, format);
    }

    private void initTexture(int buffer, int format){
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, textureId);
        glTexBuffer(GL_TEXTURE_BUFFER, format, buffer);
    }

    @Override
    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_BUFFER, textureId);
    }

    @Override
    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0+slot);
        glBindTexture(GL_TEXTURE_BUFFER, textureId);
    }

    @Override
    public void unbind() {
        glBindTexture(GL_TEXTURE_BUFFER, 0);
    }
}
