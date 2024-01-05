package entity;

import model.Model;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityLoader {

    private HashMap<Model, ArrayList<Entity>> renderList = new HashMap<>();
    private HashMap<Entity, Model> removeList = new HashMap<>();

    public void addEntity(Model model, Vector3f pos, Vector3f rot, float scale) {
        Quaternionf orientation = new Quaternionf();
        orientation.rotateLocalX(rot.x);
        orientation.rotateLocalY(rot.y);
        orientation.rotateLocalZ(rot.z);
        Entity entity = new Entity(pos, orientation, scale);
        ArrayList<Entity> list = renderList.computeIfAbsent(model, e -> new ArrayList<>());
        removeList.put(entity, model);
        list.add(entity);
    }

    public void removeEntity(Entity entity) {
        Model m = removeList.remove(entity);
        ArrayList<Entity> list = renderList.get(m);
        list.remove(entity);
    }

    public HashMap<Model, ArrayList<Entity>> getRenderList() {
        return renderList;
    }
}
