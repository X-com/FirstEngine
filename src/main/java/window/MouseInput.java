package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class MouseInput implements GLFWMouseButtonCallbackI {

    public static final boolean[] mouseButtons = new boolean[8];
    private final long window;

    private double xOffset=0, yOffset=0;

    private double x = 0, y = 0;

    public MouseInput(long window){
        this.window = window;
    }
    @Override
    public void invoke(long window, int button, int action, int mods) {
        mouseButtons[button] = action == GLFW.GLFW_PRESS;
    }

    int set = 0;
    public void update(){
        //if(set++<2) glfwSetCursorPos(window, 0, 0);
        double[] xp = new double[1], yp = new double[1];
        glfwGetCursorPos(window, xp, yp);

        x = xp[0]-xOffset;
        y = yp[0]-yOffset;
        xOffset = xp[0];
        yOffset = yp[0];
    }

    public void zeroMouse(){
        double[] xp = new double[1], yp = new double[1];
        glfwGetCursorPos(window, xp, yp);
        xOffset = xp[0];
        yOffset = yp[0];
    }

    public double getMouseX(){
        return x;
    }
    public double getMouseY(){
        return y;
    }
}
