package file;

import opengl.IndexBuffer;
import opengl.VertexArray;
import opengl.VertexBuffer;
import opengl.VertexBufferLayout;

import java.util.*;

public class ModelConverter {
    public static VertexArray extractFromObjs(ObjLoader.Obj[] objs, boolean texture, boolean norm){
        int vertIndex = 0;

        FloatAccum tempVbo = new FloatAccum();
        IntAccum tempIbo = new IntAccum();

        for(ObjLoader.Obj obj : objs){
            for (ObjLoader.Group g : obj.groups) {
                HashMap<ObjLoader.Vertex, Integer> vertexIndexMap = new HashMap<>();

                for(ObjLoader.Surface s : g.surfaces){
                    if(s.vertices.length==3) {
                        for (ObjLoader.Vertex v : s.vertices) {
                            if(!vertexIndexMap.containsKey(v)) {
                                vertexIndexMap.put(v, vertIndex);
                                appendToFloatAccum(tempVbo, v, g, texture, norm);
                                vertIndex++;
                            }
                        }
                    }
                }

                for(ObjLoader.Surface s : g.surfaces){
                    if(s.vertices.length==3) {
                        for (ObjLoader.Vertex v : s.vertices) {
                            tempIbo.add(vertexIndexMap.get(v));
                        }
                    }
                }
            }
        }

        VertexArray va = new VertexArray();
        VertexBufferLayout layout = new VertexBufferLayout();
        layout.addFloat(3);//pos
        if(texture)
            layout.addFloat(2);//tex
        if(norm)
            layout.addFloat(3);//norm

        VertexBuffer vbo = new VertexBuffer(tempVbo.getData());
        IndexBuffer ibo = new IndexBuffer(tempIbo.getData());
        va.addVertexBuffer(vbo, layout);
        va.setIndexBuffer(ibo);

        return va;
    }

    private static void appendToFloatAccum(FloatAccum vbo, ObjLoader.Vertex v, ObjLoader.Group g, boolean texture, boolean norm){
        vbo.add(g.pos, v.pos*3, 3);
        if(texture) {
            vbo.add(g.tex[v.tex * 2]);
            vbo.add(1-g.tex[v.tex*2+1]);
        }
        if(norm)
            vbo.add(g.norm, v.norm*3, 3);
    }
}
