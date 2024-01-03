package util;

import entity.Model;
import opengl.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Loader {

    private static ArrayList<VertexArray> vaos = new ArrayList<>();
    private static ArrayList<Texture> textures = new ArrayList<>();

    public static VertexArray load(float[] verticesArray, float[] textureArray, float[] normalsArray, int[] indicesArray) {
        VertexArray vaMap = new VertexArray();
        VertexBuffer vboVer = new VertexBuffer(verticesArray, 0, 3, false);
        VertexBuffer vboTex = new VertexBuffer(textureArray, 1, 2, false);
        VertexBuffer vboNor = new VertexBuffer(normalsArray, 2, 3, false);
        IndexBuffer ib = new IndexBuffer(indicesArray);
        vaMap.setIndexBuffer(ib);
        vaMap.addVertexBuffer(vboVer);
        vaMap.addVertexBuffer(vboTex);
        vaMap.addVertexBuffer(vboNor);

        vaos.add(vaMap);
        return vaMap;
    }

    public static Texture load(String imagePath) {
        InputStream is = Shader.class.getClassLoader().getResourceAsStream(imagePath);
        BufferedImage read;
        try {
            read = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(read == null) {
            throw new RuntimeException("Asset can't be loaded " + imagePath);
        }

        Texture texture = new Texture(read);

        textures.add(texture);
        return texture;
    }

    public static void dispose() {
        for (VertexArray v : vaos) {
            v.dispose();
        }
        for (Texture t : textures) {
            t.dispose();
        }
    }
}