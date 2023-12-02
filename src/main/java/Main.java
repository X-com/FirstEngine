import window.KeyInput;
import window.WindowGLFW;
import util.Config;

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
        window = new WindowGLFW(Config.WIDTH, Config.HEIGHT, "Game", true);

        do {
            window.update();
            pollClose();
        } while (running && !window.isWindowClosing());

        glfwTerminate();
    }

    private void pollClose(){
        if(KeyInput.keys[GLFW_KEY_ESCAPE]){
            running = false;
            window.close();
        }
    }
}