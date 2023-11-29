package opengl;

import util.Config;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowGLFW {

    private long window;
    private static boolean viewChanged;
    private static boolean userShowAll = true;

    public WindowGLFW() {
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }
//        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // To make MacOS happy; should not be needed
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
//        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
//        glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
//        glfwWindowHint(GLFW_MOUSE_PASSTHROUGH, GLFW_TRUE);
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(Config.MAP_WIDTH, Config.MAP_HEIGHT, "Test", NULL, NULL);
//        glfwSetWindowPos(window, Config.instance.mapTopLeftX, Config.instance.mapTopLeftY);

//        ratio = (float) util.Config.instance.mapWidth / util.Config.instance.mapHeight;
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.");
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glfwShowWindow(window);
        System.out.println("Using GL Version: " + glGetString(GL_VERSION));

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        boolean running = true;

        do {
//            checkViewChange();

//            window.swapBuffer();
        } while (running && !shouldWindowClose());

        glfwTerminate();
    }

    public static void main(String[] args) {
        new WindowGLFW();
    }

    public void swapBuffer() {
        glfwSwapBuffers(window); // Update Window
        glfwPollEvents(); // Key Mouse Input
    }

    public boolean shouldWindowClose() {
        return glfwWindowShouldClose(window);
    }

    public void show() {
        try {
            glfwShowWindow(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        try {
            glfwHideWindow(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleShowAll() {
        if (userShowAll) {
            hide();
            userShowAll = false;
        } else {
            show();
            userShowAll = true;
        }
    }

    public static void viewChanged() {
        viewChanged = true;
    }

    public void checkViewChange() {
        if (viewChanged) {
//            glfwSetWindowPos(window, util.Config.instance.mapTopLeftX, util.Config.instance.mapTopLeftY);
//            glfwSetWindowSize(window, util.Config.instance.mapWidth, util.Config.instance.mapHeight);
//            glViewport(0, 0, util.Config.instance.mapWidth, util.Config.instance.mapHeight);
//            viewChanged = false;
        }
    }
}
