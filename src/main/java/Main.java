import entity.Camera;
import entity.Entity;
import opengl.Shader;
import opengl.Texture;
import opengl.WindowGLFW;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import entity.Model;
import render.Renderer;
import util.Loader;
import util.OBJLoader;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Main {
    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    public void run() throws IOException {
        WindowGLFW window = new WindowGLFW("First", true);
        boolean running = true;

//        float[] vertices = {
//                -0.5f, 0.5f, 0f,
//                -0.5f, -0.5f, 0f,
//                0.5f, -0.5f, 0f,
//                0.5f, 0.5f, 0f,
//        };
////
//        int[] indices = {
//                0, 1, 3,
//                3, 1, 2
//        };
//
//        float[] textureCoords = {
//                0, 0,
//                0, 1,
//                1, 1,
//                1, 0
//        };

        float[] normals = new float[]{};
        float[] vertices = new float[]{-0.5F, 0.5F, 0.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, 0.5F, 0.0F, -0.5F, 0.5F, 1.0F, -0.5F, -0.5F, 1.0F, 0.5F, -0.5F, 1.0F, 0.5F, 0.5F, 1.0F, 0.5F, 0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 1.0F, 0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 0.0F, -0.5F, -0.5F, 0.0F, -0.5F, -0.5F, 1.0F, -0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 0.0F, 0.5F, 0.5F, 0.0F, 0.5F, 0.5F, 1.0F, -0.5F, -0.5F, 1.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 1.0F};
        float[] textureCoords = new float[]{0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F};
        int[] indices = new int[]{0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19, 17, 18, 20, 21, 23, 23, 21, 22};


//        shader.bindAttribute(0, "position");
        Renderer renderer = new Renderer();

        Model model = new Model(Loader.load(vertices, textureCoords, normals, indices), Loader.load("model/image.png"));
        Model model2 = new Model(OBJLoader.loadObjModel("model/stall.obj"), Loader.load("model/stallTexture.png"));

        Entity entity = new Entity(model, new Vector3f(0.0F, 0.0F, -55.0F), new Quaternionf(), 1.0F);
        Entity entity2 = new Entity(model2, new Vector3f(0.0F, 2.0F, -55.0F), new Quaternionf(), 1.0F);
        Entity entity3 = new Entity(model2, new Vector3f(0.0F, 2.0F, 55.0F), new Quaternionf(), 1.0F);
        Entity entity4 = new Entity(model2, new Vector3f(50.0F, 2.0F, 0), new Quaternionf(), 1.0F);
        Entity entity5 = new Entity(model2, new Vector3f(-50.0F, 2.0F, 0), new Quaternionf(), 1.0F);
        Camera cam = new Camera();

        do {
            cam.update();
            renderer.prepare();

            renderer.render(entity, cam);
            renderer.render(entity2, cam);
            renderer.render(entity3, cam);
            renderer.render(entity4, cam);
            renderer.render(entity5, cam);

            window.update();
        } while (running && !window.isWindowClosing());

        renderer.dispose();
        Loader.dispose();

        glfwTerminate();
    }
}