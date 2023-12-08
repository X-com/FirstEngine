import file.ModelConverter;
import file.ObjLoader;
import opengl.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import util.Config;
import window.KeyInput;
import window.WindowGLFW;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
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

        ObjLoader.Obj obj = ObjLoader.loadObjModel("models/alfa174.obj");
        VertexArray vao = ModelConverter.extractFromObjs(obj, false, true);
        Shader shader = new Shader("shader/test.vert", "shader/test.frag");

        shader.bind();

        Matrix4f mvp = new Matrix4f().ortho(-100, 100, -100, 100, 100, -100)
                .rotate((float) (Math.PI/2), 0, 0, 1)
                .rotate((float) (Math.PI/2), 0, 1, 0)
                .translate(0, 0, 0);

        Texture texture = new Texture(img, true);
        glEnable(GL_DEPTH_TEST);



        do {
            glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            texture.bind(0);

            shader.setUniformMat4f("u_mvp", mvp);
            shader.setUniform1i("tex", 0);

            shader.bind();
            vao.bind();

            glDrawElements(GL_TRIANGLES, vao.getIndexBuffer().getCount(), GL_UNSIGNED_INT, 0);


            //GLRenderer.draw(vao, vao.getIndexBuffer(), shader);

            window.update();
            checkClose();
        } while (running && !window.isWindowClosing());

        window.close();
        glfwTerminate();
    }

    private void checkClose(){
        if(KeyInput.keys[GLFW_KEY_ESCAPE]){
            running = false;
        }
    }
}