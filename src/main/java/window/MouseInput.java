package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseInput implements GLFWMouseButtonCallbackI {

    public static final boolean[] mouseButtons = new boolean[8];
    @Override
    public void invoke(long window, int button, int action, int mods) {
        mouseButtons[button] = action == GLFW.GLFW_PRESS;
    }
}
