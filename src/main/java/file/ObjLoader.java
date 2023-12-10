package file;

import render.opengl.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class ObjLoader {
    public static ObjComponent loadObjModel(String path){
        ObjComponent obj = null;
        try {
            InputStream is = Shader.class.getClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            obj = extractData(br);
        } catch (IOException e){
            e.printStackTrace();
        }
        return obj;
    }

    private static ObjComponent extractData(BufferedReader br) throws IOException {

        FloatAccum pos = new FloatAccum(),
                tex = new FloatAccum(),
                norm = new FloatAccum();

        ObjComponentData obj = new ObjComponentData(),
                subObj = null,
                group,
                curr = obj;

        for(String line = br.readLine(); line != null; line = br.readLine()){
            String[] split = line.split("\\s+");

            StringBuilder name;

            switch (split[0]){
                case "o"://object
                    name = new StringBuilder();
                    for(int i = 1; i < split.length; i++){
                        name.append(split[i]).append(" ");
                    }
                    curr = subObj = new ObjComponentData();
                    subObj.name = name.toString();
                    obj.subObj.add(subObj);
                    break;

                case "g"://group
                    name = new StringBuilder();
                    for(int i = 1; i < split.length; i++){
                        name.append(split[i]).append(" ");
                    }
                    curr = group = new ObjComponentData();
                    group.name = name.toString();
                    (subObj==null ? obj : subObj).subObj.add(group);
                    break;

                case "v"://vertex
                    for(int i = 1; i < 4; i++){
                        pos.add(Float.parseFloat(split[i]));
                    }
                    break;

                case "vt"://texture
                    for(int i = 1; i < 3; i++){
                        tex.add(Float.parseFloat(split[i]));
                    }
                    break;

                case "vn"://normal
                    for(int i = 1; i < 4; i++){
                        norm.add(Float.parseFloat(split[i]));
                    }
                    break;

                case "f":
                    ObjComponent.Vertex[] vertices = new ObjComponent.Vertex[split.length-1];
                    for (int i = 1; i < split.length; i++) {
                        String[] triangleSplit = split[i].split("/");
                        ObjComponent.Vertex vertex = new ObjComponent.Vertex();
                        vertex.pos = Integer.parseInt(triangleSplit[0])-1;
                        vertex.tex = triangleSplit[1].isEmpty() ? -1 : Integer.parseInt(triangleSplit[1])-1;
                        vertex.norm = triangleSplit.length==3 ? Integer.parseInt(triangleSplit[2])-1 : -1;
                        vertices[i-1] = vertex;
                    }
                    curr.faces.add(new ObjComponent.Surface(vertices));
                    break;
            }
        }
        return new ObjComponent(obj, pos.getData(), tex.getData(), norm.getData());
    }

    protected static class ObjComponentData{
        String name = "";
        ArrayList<ObjComponentData> subObj = new ArrayList<>();
        ArrayList<ObjComponent.Surface> faces = new ArrayList<>();

    }
}