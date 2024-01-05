import entity.Camera;
import entity.Entity;
import entity.EntityLoader;
import opengl.WindowGLFW;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import model.Model;
import render.Renderer;
import model.Loader;
import util.OBJLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        WindowGLFW window = new WindowGLFW("First", true);
        boolean running = true;

        EntityLoader loadedEntitys = new EntityLoader();
        Renderer renderer = new Renderer(loadedEntitys);
        Camera cam = new Camera();

        // temp
        float[] normals = new float[]{};
        float[] vertices = new float[]{-0.5F, 0.5F, 0.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, 0.5F, 0.0F, -0.5F, 0.5F, 1.0F, -0.5F, -0.5F, 1.0F, 0.5F, -0.5F, 1.0F, 0.5F, 0.5F, 1.0F, 0.5F, 0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 1.0F, 0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 0.0F, -0.5F, -0.5F, 0.0F, -0.5F, -0.5F, 1.0F, -0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 0.0F, 0.5F, 0.5F, 0.0F, 0.5F, 0.5F, 1.0F, -0.5F, -0.5F, 1.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 1.0F};
        float[] textureCoords = new float[]{0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F};
        int[] indices = new int[]{0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19, 17, 18, 20, 21, 23, 23, 21, 22};
        Model model = new Model(Loader.load(vertices, textureCoords, normals, indices), Loader.load("model/image.png"));

        for (int i = 0; i < 1000; i++) {
            float x = (float) (Math.random() * 100) - 50f;
            float y = (float) (Math.random() * 100) - 50f;
            float z = (float) (Math.random() * 100) - 50f;
            Vector3f pos = new Vector3f(x, y, z);

            float rx = (float) (Math.random() * 360);
            float ry = (float) (Math.random() * 360);
            float rz = (float) (Math.random() * 360);
            Vector3f rot = new Vector3f(rx, ry, rz);

            float scale = (float) (Math.random() * 2) + 1f;
            loadedEntitys.addEntity(model, pos, rot, scale);
        }
        // temp end

        do {
            cam.update();
            renderer.prepare();

            renderer.render(cam);

            window.update();
        } while (running && !window.isWindowClosing());

        renderer.dispose();
        Loader.dispose();

        glfwTerminate();
    }
}