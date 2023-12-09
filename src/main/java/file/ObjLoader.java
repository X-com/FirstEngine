package file;

import render.opengl.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ObjLoader {
    public static Obj loadObjModel(String path){
        Obj obj = null;
        try {
            InputStream is = Shader.class.getClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            obj = extractData(br);
        } catch (IOException e){
            e.printStackTrace();
        }
        return obj;
    }

    private static Obj extractData(BufferedReader br) throws IOException {
        ArrayList<SubObj> subObjs = new ArrayList<>();
        ArrayList<Group> subObjGroups = new ArrayList<>();
        ArrayList<Group> outerScopeGroups = new ArrayList<>();
        ScopeData[] data = {new ScopeData(), new ScopeData(), new ScopeData(), new ScopeData()};

        int scope = 0;

        for(String line = br.readLine(); line != null; line = br.readLine()){
            String[] split = line.split("\\s+");

            StringBuilder name;
            Group group;

            switch (split[0]){
                case "o"://everything after this point is in an object
                    switch (scope){

                        case 1://first object, with group before
                            group = new Group(data[1]);
                            data[1].clear();
                            outerScopeGroups.add(group);
                        case 0:
                            break;

                        case 3://new object with group from previous object
                            group = new Group(data[3]);
                            data[3].clear();
                            subObjGroups.add(group);
                        case 2://new object, with previous object
                            SubObj newObject = new SubObj(data[2], subObjGroups.toArray(new Group[0]));
                            subObjGroups.clear();;
                            subObjs.add(newObject);
                    }//in the case of first object no prior groups, do nothing

                    name = new StringBuilder();
                    for (int i = 1; i < split.length; i++) {
                        name.append(split[i]);
                    }
                    scope = 2;
                    data[scope].name = name.toString();
                    break;

                case "g":
                    switch (scope){
                        case 0://first group outside object
                            scope = 1;
                        case 1://group outside object
                            group = new Group(data[1]);
                            data[1].clear();
                            outerScopeGroups.add(group);
                            break;
                        case 2://first group inside object
                            scope = 3;
                            break;
                        case 3://group inside object
                            group = new Group(data[3]);
                            data[3].clear();
                            subObjGroups.add(group);
                    }
                    name = new StringBuilder();
                    for (int i = 1; i < split.length; i++) {
                        name.append(split[i]);
                    }
                    data[scope].name = name.toString();
                    break;

                case "v":
                    data[scope].pos.add(Float.parseFloat(split[1]));
                    data[scope].pos.add(Float.parseFloat(split[2]));
                    data[scope].pos.add(Float.parseFloat(split[3]));
                    break;

                case "vt":
                    data[scope].tex.add(Float.parseFloat(split[1]));
                    data[scope].tex.add(Float.parseFloat(split[2]));
                    break;

                case "vn":
                    data[scope].norm.add(Float.parseFloat(split[1]));
                    data[scope].norm.add(Float.parseFloat(split[2]));
                    data[scope].norm.add(Float.parseFloat(split[3]));
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
                    data[scope].surfaces.add(new Surface(vertices));
                    break;
            }
        }
        Group group;
        switch (scope){
            case 0://nothing
                break;
            case 1://outer scope group
                group = new Group(data[1]);
                outerScopeGroups.add(group);
                data[1].clear();
                break;

            case 3://group inside object
                group = new Group(data[3]);
                subObjGroups.add(group);
                data[3].clear();
            case 2://object
                SubObj obj = new SubObj(data[2], subObjGroups.toArray(new Group[0]));
                data[2].clear();
                subObjGroups.clear();
                subObjs.add(obj);
        }

        return new Obj(data[0], outerScopeGroups.toArray(new Group[0]), subObjs.toArray(new SubObj[0]));
    }

    private static class ScopeData {
        FloatAccum pos, tex, norm;
        ArrayList<Surface> surfaces;
        String name;
        ScopeData(){
            pos = new FloatAccum();
            tex = new FloatAccum();
            norm = new FloatAccum();
            name = "";
            surfaces = new ArrayList<>();
        }

        void clear(){
            name = "";
            pos.clear();
            tex.clear();
            norm.clear();
            surfaces.clear();
        }

        boolean empty(){
            return pos.empty() && tex.empty() && norm.empty() && surfaces.isEmpty() && Objects.equals(name, "");
        }
    }

    public static class Obj {
        public final float[] pos, tex, norm;
        public final Surface[] surfaces;

        public final Group[] groups;
        public final SubObj[] subObjs;
        Obj(ScopeData data, Group[] groups, SubObj[] subObjs){
            this.pos = data.pos.getData();
            this.tex = data.tex.getData();
            this.norm = data.norm.getData();
            this.surfaces = data.surfaces.toArray(new Surface[0]);
            this.subObjs = subObjs;
            this.groups = groups;
        }
    }

    public static class SubObj {
        public Group[] groups;

        public final float[] pos, tex, norm;
        public final Surface[] surfaces;
        public String name;
        SubObj(ScopeData data, Group[] groups){
            this.groups = groups;
            this.name = data.name;
            this.pos = data.pos.getData();
            this.tex = data.tex.getData();
            this.norm = data.norm.getData();
            this.surfaces = data.surfaces.toArray(new Surface[0]);
        }
    }

    public static class Group {
        public final float[] pos, tex, norm;
        public final Surface[] surfaces;
        public final String name;
        Group(ScopeData data){
            this.pos = data.pos.getData();
            this.tex = data.tex.getData();
            this.norm = data.norm.getData();
            this.surfaces = data.surfaces.toArray(new Surface[0]);
            this.name = data.name;
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