package controller;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Mouse {

    public final static int MOUSE_LEFT_BUTTON = 0;
    public final static int MOUSE_RIGHT_BUTTON = 1;
    public final static int MOUSE_MIDDLE_BUTTON = 2;

    private static Mouse instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean isDragging;

    private int mouseButtonDown = 0;

    static {
        instance = new Mouse();
    }

    private Mouse() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static void clear() {
        instance.scrollX = 0.0;
        instance.scrollY = 0.0;
        instance.xPos = 0.0;
        instance.yPos = 0.0;
        instance.lastX = 0.0;
        instance.lastY = 0.0;
        instance.mouseButtonDown = 0;
        instance.isDragging = false;
        Arrays.fill(instance.mouseButtonPressed, false);
    }

    public static void clearMove() {
        instance.lastX = instance.xPos;
        instance.lastY = instance.yPos;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
//        if (!Window.RELEASE_BUILD) {
//            if (!Window.getImguiLayer().getGameViewWindow().getWantCaptureMouse()) {
//                clear();
//            }
//        }

        if (instance.mouseButtonDown > 0) {
            instance.isDragging = true;
        }

//        instance.lastX = instance.xPos;
//        instance.lastY = instance.yPos;
        instance.xPos = xpos;
        instance.yPos = ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            instance.mouseButtonDown++;

            if (button < instance.mouseButtonPressed.length) {
                instance.mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            instance.mouseButtonDown--;

            if (button < instance.mouseButtonPressed.length) {
                instance.mouseButtonPressed[button] = false;
                instance.isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        instance.scrollX = xOffset;
        instance.scrollY = yOffset;
    }

    public static float getX() {
        return (float) instance.xPos;
    }

    public static float getDx() {
        float dx = (float) (instance.lastX - instance.xPos);
        instance.lastX = instance.xPos;
        return dx;
    }

    public static float getY() {
        return (float) instance.yPos;
    }

    public static float getDy() {
        float dx = (float) (instance.lastY - instance.yPos);
        instance.lastY = instance.yPos;
        return dx;
    }

    public static float getScrollX() {
        return (float) instance.scrollX;
    }

    public static float getScrollY() {
        return (float) instance.scrollY;
    }

    public static boolean isDragging() {
        return instance.isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < instance.mouseButtonPressed.length) {
            return instance.mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
