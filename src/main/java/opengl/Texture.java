package opengl;

import temp.Loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12C.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;

public class Texture {
    private int textureId;

    /**
     * Width of the texture.
     */
    private int width;
    /**
     * Height of the texture.
     */
    private int height;

    public Texture() {
        textureId = glGenTextures();
    }

    public Texture(BufferedImage image) {
        textureId = glGenTextures();
        width = image.getWidth();
        height = image.getHeight();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
            image = convertToBufferedImage(image, BufferedImage.TYPE_INT_ARGB);
        }

        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        pixels = convertPixels(pixels);
        IntBuffer intBuffer = createByteBuffer(pixels);

        uploadToGRam(image, intBuffer);
    }

    public static BufferedImage convertToBufferedImage(BufferedImage image, int imageType) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        Graphics2D g2 = result.createGraphics();
        g2.drawImage(image, null, 0, 0);
        return result;
    }

    public static Texture createTexture(int width, int height, ByteBuffer data) {
        Texture texture = new Texture();
        texture.setWidth(width);
        texture.setHeight(height);

        texture.bind();

        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        texture.uploadToGRam(GL_RGBA8, width, height, GL_RGBA, data);

        return texture;
    }

    public void uploadToGRam(int internalFormat, int width, int height, int format, ByteBuffer data) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void uploadToGRam(BufferedImage image, IntBuffer intBuffer) {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, intBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Gets the texture width.
     *
     * @return Texture width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the texture width.
     *
     * @param width The width to set
     */
    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    /**
     * Gets the texture height.
     *
     * @return Texture height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the texture height.
     *
     * @param height The height to set
     */
    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        }
    }

    /**
     * Sets a parameter of the texture.
     *
     * @param name  Name of the parameter
     * @param value Value to set
     */
    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_2D, name, value);
    }

    public static BufferedImage readImageFromFile(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            BufferedImage formatted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            formatted.getGraphics().drawImage(image, 0, 0, null);
            return formatted;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read image: \"" + path + "\"");
        }
    }

    public static Texture readImageFromResources(String fileName) {
        try {
            InputStream is = Loader.class.getClassLoader().getResourceAsStream(fileName);
            BufferedImage img = ImageIO.read(is);
            return new Texture(img);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load texture " + fileName + ".png.");
            System.exit(-1);
        }
        return null;
    }

    // converts argb to rgba
    public static int[] convertPixels(int[] pixels) {
        int[] rgba = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb & 0xff000000) >> 24;
            int r = (argb & 0xff0000) >> 16;
            int g = (argb & 0xff00) >> 8;
            int b = (argb & 0xff);
            rgba[i] = a << 24 | b << 16 | g << 8 | r;
        }
        return rgba;
    }

    public static IntBuffer createByteBuffer(int[] data) {
        IntBuffer result = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(data).flip();
        return result;
    }

    public Integer getTextureId() {
        return textureId;
    }

    public void dispose() {
        glDeleteTextures(textureId);
    }
}
