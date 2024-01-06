package entity;

import controller.Keyboard;
import controller.Mouse;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {

    private final Vector3f position = new Vector3f(0, 0, 0);
    private final Quaternionf orientation = new Quaternionf(0, 0, 0, 1);
    private final static Vector3f mutableVert = new Vector3f();
    private final static Quaternionf mutableQuat = new Quaternionf();
    private final static Matrix4f mutableMat = new Matrix4f();
    private final float SENSITIVITY = .3f;

    public Camera() {
    }

    public void update() {
        if (Keyboard.isKeyPressed(Keyboard.KEY_W)) {
            position.add(orientation.positiveZ(mutableVert).negate());
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_S)) {
            position.add(orientation.positiveZ(mutableVert));
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_D)) {
            position.add(orientation.positiveX(mutableVert));
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_A)) {
            position.add(orientation.positiveX(mutableVert).negate());
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_SPACE)) {
            position.add(orientation.positiveY(mutableVert));
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_LEFT_SHIFT)) {
            position.add(orientation.positiveY(mutableVert).negate());
        }

        if (Keyboard.isKeyPressed(Keyboard.KEY_RIGHT)) {
            orientation.rotateAxis((float) Math.toRadians(SENSITIVITY), new Vector3f(0, 1, 0));
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_LEFT)) {
            orientation.rotateAxis((float) Math.toRadians(-SENSITIVITY), new Vector3f(0, 1, 0));
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_UP)) {
            orientation.rotateAxis((float) Math.toRadians(-SENSITIVITY), orientation.positiveX(mutableVert));
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_DOWN)) {
            orientation.rotateAxis((float) Math.toRadians(SENSITIVITY), orientation.positiveX(mutableVert));
        }

        if (Mouse.mouseButtonDown(Mouse.MOUSE_MIDDLE_BUTTON)) {
            orientation.rotateAxis((float) Math.toRadians(Mouse.getDx() * -SENSITIVITY), new Vector3f(0, 1, 0));
            orientation.rotateAxis((float) Math.toRadians(Mouse.getDy() * -SENSITIVITY), orientation.positiveX(mutableVert));
        } else {
            Mouse.clearMove();
        }
        System.out.println(position);
    }

    public Matrix4f getViewMatrix() {
        mutableMat.identity();
        mutableMat.rotate(orientation).translate(position.negate(mutableVert));
        return mutableMat;
    }
}
