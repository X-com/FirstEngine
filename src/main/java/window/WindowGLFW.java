package window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import util.Config;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowGLFW {

    private long window;
    private final KeyInput keyCallback;
    private final MouseInput mouseCallback;
    private boolean vSync;
    private boolean anchored;

    public WindowGLFW(int width, int height, String title, boolean vSync, int version) {
        this.vSync = vSync;

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        anchored = false;
        setOpenGLHints(version);
        createWindow(width, height, title);
        centerWindowOnScreen(width, height);

        setOpenGlContext(window);
        glfwFocusWindow(window);
        if (vSync) {
            glfwSwapInterval(1);
        }
        mouseCallback = new MouseInput(window);
        keyCallback = new KeyInput();
        setCallbacks();
        mouseCallback.onFocus();
        glfwShowWindow(window);
    }

    public void setCursorAnchored(boolean anchored){
        if(anchored){
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            //glfwSetCursorPos(window, 0, 0);
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            //glfwSetCursorPos(window, Config.WIDTH/2d, Config.HEIGHT/2d);
        }
        this.anchored = anchored;
    }

    public boolean isCursorAnchored() {
        return anchored;
    }

    private void setCallbacks() {
        glfwSetKeyCallback(window, keyCallback);
        glfwSetMouseButtonCallback(window, mouseCallback);
        glfwSetWindowFocusCallback(window, ((window1, focused) -> {
            if(focused) mouseCallback.onFocus();
        }));
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

    private static void setOpenGLHints(int version) {
        if (version == 3) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        } else if (version == 2) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        } else {
            throw new RuntimeException("OpenGL 3.3 nor OpenGL 2.1 is supported. Update graphics drivers");
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

    public void pollEvents() {
        glfwPollEvents(); // Key Mouse Input
        mouseCallback.update();
    }

    public boolean isWindowClosing() {
        return glfwWindowShouldClose(window);
    }

    public void setVSync(boolean vSync) {
        this.vSync = vSync;
        if (vSync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    public void getSize(int[] dim){
        int[] w = new int[1], h = new int[1];
        glfwGetFramebufferSize(window, w, h);
        dim[0] = w[0];
        dim[1] = h[0];
    }

    public boolean isVSyncEnabled() {
        return vSync;
    }

    public void close() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }
}
