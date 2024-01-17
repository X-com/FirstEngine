import file.ModelConverter;
import file.ObjComponent;
import file.ObjLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import render.Camera;
import render.opengl.*;
import util.Config;
import window.KeyInput;
import window.MouseInput;
import window.WindowGLFW;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

public class RaytraceTest {
    private boolean running;
    private WindowGLFW window;
    public static void main(String[] args) {
        new RaytraceTest().run();
    }

    public void run() {

        BufferedImage img;
        try {//have to do before init opengl or imageio read hangs indefinitely on osx
            img = ImageIO.read(Objects.requireNonNull(RaytraceTest.class.getResourceAsStream("texture/stallTexture.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(img.getType() != BufferedImage.TYPE_INT_ARGB){
            BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            img2.getGraphics().drawImage(img, 0, 0, null);
            img = img2;
        }

        window = new WindowGLFW(Config.WIDTH, Config.HEIGHT, "Game", true, 3);
        window.setCursorAnchored(true);
        Camera camera = new Camera((float) (120*Math.PI/180), 0.1f, 1000);

        glEnable(GL_DEPTH_TEST);

        ObjComponent stallObj = ObjLoader.loadObjModel("models/stall.obj");
        float[] triangles = ModelConverter.extractTriangles(stallObj, true, true);//24 floats/triangle

        Texture2D stallTexture = new Texture2D(img, true);
        TextureBuffer stallTriangles = new TextureBuffer(triangles, GL_RGBA32F);
        ITexture[] textures = {stallTexture, stallTriangles};

        VertexArray va = fullScreenRect();

        Shader shader = new Shader("shader/raytrace_test/rtx.vert", "shader/raytrace_test/rtx.frag");
        shader.setUniform1i("tex", 0);
        shader.setUniform1i("triangles", 1);
        shader.setUniform1i("u_numTriangles", triangles.length/24);
        System.out.println(triangles.length/24);

        running = true;
        int count = 0;
        long lastTime = System.nanoTime();
        do {
            window.pollEvents();
            count++;
            glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            shader.setUniformMat4f("u_proj", camera.getProjection());
            shader.setUniformMat4f("u_view", camera.getView());
            Vector3f eye = camera.getPosition();
            shader.setUniform3f("u_eye", eye.x, eye.y, eye.z);

            GLRenderer.draw(va, va.getIndexBuffer(), shader, textures);

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
        window.close();
    }

    private VertexArray fullScreenRect(){
        VertexBuffer vbo = new VertexBuffer(new float[]{
                -1, -1,
                1, -1,
                1, 1,
                -1, 1
        });
        VertexBufferLayout layout = new VertexBufferLayout();
        layout.addFloat(2);

        IndexBuffer ibo = new IndexBuffer(new int[]{
                0, 1, 2,
                2, 3, 0
        });
        VertexArray va = new VertexArray();
        va.addVertexBuffer(vbo, layout);
        va.setIndexBuffer(ibo);
        return va;
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
            float v = 0.2f;
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
}