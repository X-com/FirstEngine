package render;

import entity.Camera;
import entity.Entity;
import opengl.Shader;
import opengl.WindowGLFW;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import entity.Model;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;
    private Shader shader;
    private static Matrix4f mutableMat;

    public Renderer() {
        shader = new Shader("shader/vertexShader", "shader/fragmentShader");
        createProjectionMatrix();
        shader.bind();
        shader.setUniformMat4f("projMat", projectionMatrix);
        shader.unbind();
        mutableMat = new Matrix4f();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1);
    }

    public void render(Entity entity, Camera cam) {
        shader.bind();
        Model model = entity.getModel();
        model.bind();

        Matrix4f transformationMatrix = mutableMat.translationRotateScale(entity.getPosition(), entity.getOrientation(), entity.getScale());
        shader.setUniformMat4f("transMat", transformationMatrix);
        Matrix4f viewMatrix = cam.getViewMatrix();
        shader.setUniformMat4f("viewMat", viewMatrix);

        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        model.unbind();

        shader.unbind();
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) WindowGLFW.WIDTH / (float) WindowGLFW.HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

    public void dispose() {
        shader.dispose();
    }
}