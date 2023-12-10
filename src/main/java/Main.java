import file.ModelConverter;
import file.ObjComponent;
import file.ObjLoader;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import render.Camera;
import render.opengl.Shader;
import render.opengl.Texture;
import render.opengl.VertexArray;
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

public class Main {
    private boolean running;
    private WindowGLFW window;
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {

        BufferedImage img;
        try {//have to do before init opengl or imageio read hangs indefinitely on osx
            img = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("texture/stallTexture.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(img.getType() != BufferedImage.TYPE_INT_ARGB){
            BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            img2.getGraphics().drawImage(img, 0, 0, null);
            img = img2;
        }

        running = true;
        window = new WindowGLFW(Config.WIDTH, Config.HEIGHT, "Game", true, 3);
        window.setCursorAnchored(true);

        ObjComponent obj = ObjLoader.loadObjModel("models/alfa174.obj");
        VertexArray vao = ModelConverter.extractFromObj(obj, false, true);
        Shader shader = new Shader("shader/test.vert", "shader/test.frag");

        shader.bind();

        Texture texture = new Texture(img, true);
        glEnable(GL_DEPTH_TEST);
        Camera camera = new Camera((float) (120*Math.PI/180), 0.1f, 1000);
        Vector3f startPos = new Vector3f((float) (Math.random()*200-100), (float) (Math.random()*200-100), (float) (Math.random()*200-100));
        startPos = new Vector3f(startPos).normalize().mul(100).add(startPos);
        camera.goTo(startPos);
        camera.lookAt(new Vector3f());
        shader.setUniformMat4f("u_mvp", camera.getMvp());

        do {
            window.pollEvents();
            //mvp = mvp.translate(0, 1, 0);
            glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            texture.bind(0);
            //camera.rotate(0.01f, 0);
            //camera.move(new Vector3f());

            shader.setUniformMat4f("u_mvp", camera.getMvp());
            //shader.setUniform1i("tex", 0);

            shader.bind();
            vao.bind();

            glDrawElements(GL_TRIANGLES, vao.getIndexBuffer().getCount(), GL_UNSIGNED_INT, 0);

            checkAnchor();
            checkClose();
            handleMovement(camera);

            int[] dim = new int[2];
            window.getSize(dim);
            camera.setAspect((float)dim[0]/dim[1]);
            window.swapBuffers();
        } while (running && !window.isWindowClosing());
        window.close();
    }

    private void checkClose(){
        if(KeyInput.keys[GLFW_KEY_ESCAPE]){
            running = false;
        }
    }

    private void handleMovement(Camera camera){
        if(window.isCursorAnchored()) {

            camera.rotate(MouseInput.dx / 500f, MouseInput.dy / 500f);
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

            camera.moveForward(new Vector3f(dx, dy, dz));
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