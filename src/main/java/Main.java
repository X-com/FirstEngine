import file.ModelConverter;
import file.ObjComponent;
import file.ObjLoader;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import render.Camera;
import render.opengl.GLRenderer;
import render.opengl.Shader;
import render.opengl.Texture2D;
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

        ObjComponent stallObj = ObjLoader.loadObjModel("models/stall.obj");
        VertexArray stallVao = ModelConverter.extractVertexArray(stallObj, true, true);
        Shader stallShader = new Shader("shader/basic/test.vert", "shader/basic/test.frag");

        stallShader.bind();

        Texture2D stallTexture = new Texture2D(img, true);
        glEnable(GL_DEPTH_TEST);
        Camera camera = new Camera((float) (120*Math.PI/180), 0.1f, 1000);
        Vector3f startPos = new Vector3f((float) (Math.random()*2-1), (float) (Math.random()*2-1), (float) (Math.random()*2-1));
        startPos = new Vector3f(startPos).normalize().mul(4).add(startPos);
        camera.goTo(startPos);
        camera.lookAt(new Vector3f());
        stallShader.setUniformMat4f("u_mvp", camera.getMvp());

        long lastTime = System.nanoTime();
        int count = 0;
        do {
            window.pollEvents();
            count++;
            glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            stallShader.setUniformMat4f("u_mvp", camera.getMvp());
            GLRenderer.draw(stallVao, stallVao.getIndexBuffer(), stallShader, stallTexture);

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