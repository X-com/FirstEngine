package model;

import com.badlogic.gdx.graphics.g3d.model.data.ModelNode;

import java.util.HashMap;

public enum ModelEnum {
    STALL(0, "model/stall.obj", "model/stallTexture.png"),
    ;

    private static HashMap<Integer, ModelEnum> models;

    static {
        models = new HashMap<>();
        for (ModelEnum m : values()) {
            models.put(m.id, m);
        }
    }

    public int id;
    public String mesh;
    public String tex;

    ModelEnum(int id, String mesh, String tex) {
        this.id = id;
        this.mesh = mesh;
        this.tex = tex;
    }

    public static String getMeshName(int id) {
        return models.get(id).mesh;
    }

    public static String getTextureName(int id) {
        return models.get(id).tex;
    }
}
