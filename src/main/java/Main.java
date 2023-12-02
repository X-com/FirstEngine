import file.ModelConverter;
import file.ObjLoader;
import opengl.*;
import org.joml.Matrix4f;
import util.Config;
import window.KeyInput;
import window.WindowGLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Main {
    private boolean running;
    private WindowGLFW window;
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        running = true;
        window = new WindowGLFW(Config.WIDTH, Config.HEIGHT, "Game", true, 3);

        VertexArray vao = ModelConverter.extractFromObjs(ObjLoader.loadObjModel("models/dragon.obj"), false, false);

        Shader shader = new Shader("shader/test.vert", "shader/test.frag");

        shader.bind();

        Matrix4f mvp = new Matrix4f().ortho(-10, 10, -10, 10, 10, -10).translate(0, -1, 0);

        shader.setUniformMat4f("u_mvp", mvp);
        do {

            GLRenderer.draw(vao, vao.getIndexBuffer(), shader);

            window.update();
            checkClose();
        } while (running && !window.isWindowClosing());

        window.close();
        glfwTerminate();
    }

    private void checkClose(){
        if(KeyInput.keys[GLFW_KEY_ESCAPE]){
            running = false;
        }
    }
}