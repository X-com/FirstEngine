import file.FloatAccum;
import file.IntAccum;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import render.Camera;
import render.opengl.*;
import util.Config;
import window.KeyInput;
import window.MouseInput;
import window.WindowGLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class SphereTest {

    private static WindowGLFW window;
    private static boolean running;
    public static void main(String[] args) {
        new SphereTest().run();
    }

    public void run(){
        window = new WindowGLFW(Config.WIDTH, Config.HEIGHT, "sphere", true, 3);
        window.setCursorAnchored(true);
        running = true;
        VertexArray va = getGridVa(64);
        Shader shader = new Shader("shader/sphere/sphere.vert", "shader/sphere/sphere.frag");
        shader.setUniform1f("u_r", 10);

        glEnable(GL_DEPTH_TEST);

        Camera camera = new Camera((float) (120*Math.PI/180), 0.1f, 1000);
        camera.lookAt(new Vector3f());
        shader.setUniformMat4f("u_mvp", camera.getMvp());

        long lastTime = System.nanoTime();
        int count = 0;
        do {
            window.pollEvents();

            count++;
            glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.setUniformMat4f("u_mvp", camera.getMvp());
            GLRenderer.draw(va, va.getIndexBuffer(), shader);

            checkAnchor();
            checkClose();
            handleMovement(camera);

            int[] dim = new int[2];
            window.getSize(dim);
            camera.setAspect((float)dim[0]/dim[1]);
            window.swapBuffers();
            long now = System.nanoTime();
            if(now-lastTime>1000000000){
                System.out.printf("%.3fms%n", (now-lastTime)/1e6/count);
                lastTime = now;
                count = 0;
            }

        } while (running && !window.isWindowClosing());
    }

    private void checkClose(){
        if(KeyInput.keys[GLFW_KEY_ESCAPE]){
            running = false;
        }
    }

    private void handleMovement(Camera camera){
        if(window.isCursorAnchored()) {

            camera.rotate(-MouseInput.dx / 500f, -MouseInput.dy / 500f);
            float dx = 0, dy = 0, dz = 0;

            if(KeyInput.keys[GLFW_KEY_W]){
                dz -= 1;
            }
            if(KeyInput.keys[GLFW_KEY_S]){
                dz += 1;
            }

            if(KeyInput.keys[GLFW_KEY_A]){
                dx -= 1;
            }
            if(KeyInput.keys[GLFW_KEY_D]){
                dx += 1;
            }

            if(KeyInput.keys[GLFW_KEY_LEFT_SHIFT]){
                dy -= 1;
            }
            if(KeyInput.keys[GLFW_KEY_SPACE]){
                dy += 1;
            }
            float v = 0.02f;
            camera.moveForward(new Vector3f(dx, dy, dz).mul(v));
        }
    }

    private boolean kPressed = false;
    private void checkAnchor(){
        if(KeyInput.keys[GLFW_KEY_K]){
            if(!kPressed) {
                kPressed = true;
                window.setCursorAnchored(!window.isCursorAnchored());
            }
        } else {
            kPressed = false;
        }
    }

    private static VertexArray getGridVa(int n){
        FloatAccum vbo = new FloatAccum();
        for (int i = 0; i < n+1; i++) {
            for (int j = 0; j < n+1; j++) {
                float x = 2f*i/n-1,
                        y = 2f*j/n-1;
                float r = (float) Math.random(),
                        g = (float) Math.random(),
                        b = (float) Math.random();
                vbo.add(x, y, r, g, b);
            }
        }

        IntAccum ibo = new IntAccum();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int i00 = i*(n+1)+j,
                        i10 = (i+1)*(n+1)+j,
                        i11 = (i+1)*(n+1)+j+1,
                        i01 = i*(n+1)+j+1;
                ibo.add(i00, i01, i11, i11, i10, i00);
            }
        }
        VertexBuffer vertexBuffer = new VertexBuffer(vbo.getData());
        IndexBuffer indexBuffer = new IndexBuffer(ibo.getDataCopy());

        VertexBufferLayout layout = new VertexBufferLayout();
        layout.addFloat(2);//pos
        layout.addFloat(3);//color

        VertexArray va = new VertexArray();
        va.addVertexBuffer(vertexBuffer, layout);
        va.setIndexBuffer(indexBuffer);
        return va;
    }
}
