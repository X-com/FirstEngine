package file;

import opengl.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ObjLoader {
    public static Obj[] loadObjModel(String path){
        Obj[] objects = null;
        try {
            InputStream is = Shader.class.getClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            objects = extractData(br);
        } catch (IOException e){
            e.printStackTrace();
        }
        return objects;
    }

    private static Obj[] extractData(BufferedReader br) throws IOException {
        ArrayList<Obj> objects = new ArrayList<>();

        ArrayList<Group> objectData = new ArrayList<>();
        ArrayList<Surface> surfaces = new ArrayList<>();

        FloatAccum pos = new FloatAccum();
        FloatAccum tex = new FloatAccum();
        FloatAccum norm = new FloatAccum();

        StringBuilder objName = new StringBuilder();
        StringBuilder groupName = new StringBuilder();

        Group g;


        for(String line = br.readLine(); line != null; line = br.readLine()){
            if(line.startsWith("#")) continue;
            String[] split = line.split(" ");

            switch (split[0]){
                case "o":
                    if(!pos.empty()){
                        g = createGroup(pos, tex, norm, surfaces, groupName.toString());
                        objectData.add(g);
                    }
                    groupName = new StringBuilder();

                    if(!objectData.isEmpty()) {
                        objects.add(new Obj(objectData.toArray(new Group[0]), objName.toString()));
                        objectData = new ArrayList<>();
                    }
                    objName = new StringBuilder();

                    for (int i = 1; i < split.length; i++) {
                        objName.append(split[i]).append(" ");
                    }
                    break;

                case "g":
                    if(!pos.empty()) {
                        g = createGroup(pos, tex, norm, surfaces, groupName.toString());
                        objectData.add(g);
                    }
                    groupName = new StringBuilder();
                    for (int i = 1; i < split.length; i++) {
                        groupName.append(split[i]).append(" ");
                    }
                    break;

                case "v":
                    pos.add(Float.parseFloat(split[1]));
                    pos.add(Float.parseFloat(split[2]));
                    pos.add(Float.parseFloat(split[3]));
                    break;

                case "vt":
                    tex.add(Float.parseFloat(split[1]));
                    tex.add(Float.parseFloat(split[2]));
                    break;

                case "vn":
                    norm.add(Float.parseFloat(split[1]));
                    norm.add(Float.parseFloat(split[2]));
                    norm.add(Float.parseFloat(split[3]));
                    break;

                case "f":
                    Vertex[] vertices = new Vertex[split.length-1];
                    for (int i = 1; i < split.length; i++) {
                        String[] triangleSplit = split[i].split("/");
                        Vertex vertex = new Vertex();
                        vertex.pos = Integer.parseInt(triangleSplit[0])-1;
                        vertex.tex = triangleSplit[1].isEmpty() ? -1 : Integer.parseInt(triangleSplit[1])-1;
                        vertex.norm = triangleSplit.length==3?Integer.parseInt(triangleSplit[2])-1 : -1;
                        vertices[i-1] = vertex;
                    }
                    surfaces.add(new Surface(vertices));
                    break;
            }
        }
        if(!pos.empty())
            objectData.add(new Group(pos.getData(), tex.getData(), norm.getData(), surfaces.toArray(new Surface[0]), groupName.toString()));

        if(!objectData.isEmpty())
            objects.add(new Obj(objectData.toArray(new Group[0]), objName.toString()));

        return objects.toArray(new Obj[0]);
    }

    private static Group createGroup(FloatAccum pos, FloatAccum tex, FloatAccum norm, ArrayList<Surface> geometry, String name) {
        Group g = new Group(pos.getData(), tex.getData(), norm.getData(), geometry.toArray(new Surface[0]), name);
        pos.clear();
        tex.clear();
        norm.clear();
        geometry.clear();
        return g;
    }

    public static class Obj {
        public Group[] groups;
        public String name;
        public Obj(Group[] groups, String name){
            this.groups = groups;
            this.name = name;
        }
    }

    public static class Group {
        public final float[] pos, tex, norm;
        public final Surface[] surfaces;
        public final String name;
        public Group(float[] pos, float[] tex, float[] norm, Surface[] surfaces, String name){
            this.pos = pos;
            this.tex = tex;
            this.norm = norm;
            this.surfaces = surfaces;
            this.name = name;
        }
    }

    public static class Surface {
        public final Vertex[] vertices;
        public Surface(Vertex[] vertices){
            this.vertices = vertices;
        }
    }

    public static class Vertex {
        public int pos, tex, norm;

        @Override
        public boolean equals(Object b){
            if(!(b instanceof Vertex))
                return false;
            Vertex v = (Vertex) b;
            return pos==v.pos && tex==v.tex && norm==v.norm;
        }
    }
}