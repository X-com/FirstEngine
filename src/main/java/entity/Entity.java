package entity;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {

    private Model model;
    private Vector3f position;
    private Quaternionf orientation;
    private float scale;

    public Entity(Model model, Vector3f position, Quaternionf orientation, float scale) {
        this.model = model;
        this.position = position;
        this.orientation = orientation;
        this.scale = scale;
    }

    public Model getModel() {
        return model;
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
