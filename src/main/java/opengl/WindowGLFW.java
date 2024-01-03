package opengl;

import controller.Keyboard;
import controller.Mouse;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowGLFW {

    private long window;
//    private final GLFWKeyCallback keyCallback;
    private boolean vSynch;
    public static int WIDTH = 1024;
    public static int HEIGHT = 720;

    public WindowGLFW(String title, boolean vsync) {
        vSynch = vsync;

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLCapabilities c = tempWindowForVersionGrab();
        setOpenGLHints(c);
        createWindow(WIDTH, HEIGHT, title);
        centerWindowOnScreen(WIDTH, HEIGHT);
        setOpenGlContext(window);

        if (vsync) {
            glfwSwapInterval(1);
        }

//        keyCallback = createKeyMouseCallbacks();

        keyMouseCallbacks(window);

        glfwShowWindow(window);
    }

    private void keyMouseCallbacks(long window) {
        glfwSetCursorPosCallback(window, Mouse::mousePosCallback);
        glfwSetMouseButtonCallback(window, Mouse::mouseButtonCallback);
        glfwSetScrollCallback(window, Mouse::mouseScrollCallback);
        glfwSetKeyCallback(window, Keyboard::keyCallback);
        glfwSetWindowSizeCallback(window, (w, newWidth, newHeight) -> {
            WIDTH = newWidth;
            HEIGHT = newHeight;
        });
    }

    private GLFWKeyCallback createKeyMouseCallbacks() {
        final GLFWKeyCallback keyCallback;
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        };
        glfwSetKeyCallback(window, keyCallback);
        return keyCallback;
    }

    private static GLCapabilities tempWindowForVersionGrab() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long temp = glfwCreateWindow(1, 1, "", NULL, NULL);
        glfwMakeContextCurrent(temp);
        GL.createCapabilities();
        GLCapabilities caps = GL.getCapabilities();
        glfwDestroyWindow(temp);
        return caps;
    }

    private static void setOpenGLHints(GLCapabilities c) {
        if (c.OpenGL32) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        } else if (c.OpenGL21) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        } else {
            throw new RuntimeException("OpenGL 3.2 nor OpenGL 2.1 is supported. Update graphics drivers");
        }
    }

    private void centerWindowOnScreen(int width, int height) {
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
    }

    private void setOpenGlContext(long window) {
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    private void createWindow(int width, int height, String title) {
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window!");
        }
    }

    public void update() {
        glfwSwapBuffers(window); // Update Window
        glfwPollEvents(); // Key Mouse Input
    }

    public boolean isWindowClosing() {
        return glfwWindowShouldClose(window);
    }

    public void setVSync(boolean vsync) {
        vSynch = vsync;
        if (vsync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    public boolean isVSyncEnabled() {
        return vSynch;
    }

    public void destroy() {
        glfwDestroyWindow(window);
//        keyCallback.free();
    }
}
