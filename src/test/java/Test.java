import org.joml.Matrix4f;
import org.joml.Vector4f;
import render.Camera;
import util.Config;

public class Test {
    public static void main(String[] args) {
        //Camera camera = new Camera((float) (60*Math.PI/180), 0.1f, 1000);

        Matrix4f mvp = new Matrix4f().perspective((float) (60*Math.PI/180f), (float) Config.WIDTH /Config.HEIGHT, 0.1f, 1000);

        Matrix4f mvpInv = mvp.invert();
        print((mvpInv.transform(new Vector4f(1, 1, 0, 0))));//tan(fov/2)
    }

    private static Vector4f scale(Vector4f v){
        return v.mul(1/v.w);
    }

    private static void print(Vector4f v){
        System.out.printf("(%f, %f, %f, %f)%n", v.x, v.y, v.z, v.w);
    }
}
