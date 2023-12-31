package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class KeyInput implements GLFWKeyCallbackI {

    public static final boolean[] keys = new boolean[32000];
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action == GLFW.GLFW_PRESS;
    }
}
