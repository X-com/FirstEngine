import window.WindowGLFW;
import util.Config;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        WindowGLFW window = new WindowGLFW(Config.WIDTH, Config.HEIGHT, "First", true);
        boolean running = true;
        do {
            window.update();

        } while (running && !window.isWindowClosing());

        glfwTerminate();
    }
}