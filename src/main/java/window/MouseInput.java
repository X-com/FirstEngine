package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;

public class MouseInput implements GLFWMouseButtonCallbackI {

    public static final boolean[] mouseButtons = new boolean[8];
    public static int x, y, dx, dy;
    private final long window;

    private boolean focusUpdated;


    public MouseInput(long window){
        this.window = window;
        focusUpdated = false;
    }
    @Override
    public void invoke(long window, int button, int action, int mods) {
        mouseButtons[button] = action == GLFW.GLFW_PRESS;
    }

    public void update() {
        double[] xp = new double[1], yp = new double[1];
        GLFW.glfwGetCursorPos(window, xp, yp);
        double x = xp[0],
                y = yp[0];
        dx = (int) (x - MouseInput.x);
        dy = (int) (y - MouseInput.y);
        MouseInput.x = (int) x;
        MouseInput.y = (int) y;
        if(focusUpdated){
            dx = 0;
            dy = 0;
            focusUpdated = false;
        }
    }

    public void onFocus(){
        focusUpdated = true;
    }
}
