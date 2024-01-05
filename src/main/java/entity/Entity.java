package entity;

import model.Model;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {

    private Vector3f position;
    private Quaternionf orientation;
    private float scale;

    public Entity(Vector3f position, Quaternionf orientation, float scale) {
        this.position = position;
        this.orientation = orientation;
        this.scale = scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getOrientation() {
        return orientation;
    }

    public float getScale() {
        return scale;
    }
}
