package render;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import util.Config;

/**
 * operates on an xz (horizontal) y (vertical) world coordinate system
 * and the opengl default screen coordinate system
 */
public class Camera {
    private Vector3f position;
    private float yaw, pitch;

    private float aspect, fov, near, far;

    /**
     *
     * @param fov horizontal fov
     * @param far far plane distance
     */
    public Camera(float fov, float near, float far){
        setScreen(fov, (float) Config.WIDTH/Config.HEIGHT);
        this.far = far;
        this.near = near;
        position = new Vector3f();
        pitch = 0;
        yaw = 0;
    }

    public void rotate(float dYaw, float dPitch){
        pitch = (float) Math.clamp(-Math.PI/2, Math.PI/2, pitch + dPitch);
        yaw += dYaw;
        if(yaw < 0){
            yaw += (float) (2*Math.PI);
        } else if(yaw > 2*Math.PI){
            yaw -= (float) (2*Math.PI);
        }
        //yaw  = mod(yaw + dYaw, (float) (2*Math.PI));
    }

    public void lookAt(float yaw, float pitch){
        this.yaw  = mod(yaw, (float) (2*Math.PI));
        this.pitch = (float) Math.clamp(-Math.PI/2, Math.PI/2, pitch);;
    }

    //moves in forward direction
    public void moveForward(Vector3f dp){
        Matrix4f rotate = new Matrix4f().rotateY(-yaw);
        position.add(rotate.transformDirection(dp));
    }

    public void moveTo(Vector3f position){
        this.position = position;
    }
    public Matrix4f getMvp(){
        return new Matrix4f().perspective(fov, aspect, near, far)
                .rotateX(pitch)
                .rotateY(yaw)
                .translate(new Vector3f(position).negate());
    }

    /**
     *
     * @param aspect width/height
     */
    public void setAspect(float aspect) {
        setScreen(fov, aspect);
    }

    /**
     *
     * @param fov horizontal fov
     */
    public void setFov(float fov) {
        setScreen(fov, aspect);
    }

    public void setFar(float far) {
        this.far = far;
    }

    public void setNear(float near) {
        this.near = near;
    }

    private void setScreen(float fov, float aspect) {
        this.fov = 2*Math.atan2(Math.tan(fov/2), aspect);
        this.aspect = aspect;
    }

    private static float mod(float a, float mod){
        return ((a%mod)+mod)%mod;
    }
}
