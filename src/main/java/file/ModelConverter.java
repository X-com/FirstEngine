package file;

import render.opengl.IndexBuffer;
import render.opengl.VertexArray;
import render.opengl.VertexBuffer;
import render.opengl.VertexBufferLayout;

import java.util.*;

public class ModelConverter {
    public static VertexArray extractVertexArray(ObjComponent obj, boolean includeTexture, boolean includeNormals) {
        //put all vertices into a hashmap
        Map<ObjComponent.Vertex, Integer> vertexSet = new HashMap<>();
        IntAccum indexAccum = new IntAccum();
        extractVerticesAndIndexes(vertexSet, indexAccum, obj);

        float[] vertices = assembleVertexBuffer(obj, vertexSet, includeTexture, includeNormals);
        int[] indexes = indexAccum.getData();

        VertexArray va = new VertexArray();

        VertexBuffer vb = new VertexBuffer(vertices);
        VertexBufferLayout layout = new VertexBufferLayout();
        layout.addFloat(3);
        if(includeTexture) layout.addFloat(2);
        if(includeNormals) layout.addFloat(3);
        va.addVertexBuffer(vb, layout);

        IndexBuffer ib = new IndexBuffer(indexes);
        va.setIndexBuffer(ib);

        return va;
    }

    public static float[] extractTriangles(ObjComponent obj, boolean includeTexture, boolean includeNormal){
        FloatAccum triangles = new FloatAccum();
        extractTriangles(obj, triangles, includeTexture, includeNormal);
        return triangles.getData();
    }

    private static void extractTriangles(ObjComponent obj, FloatAccum triangles, boolean includeTexture, boolean includeNormal){
        for(ObjComponent.Surface face : obj.faces){
            if(face.vertices.length != 3) continue;
            for(ObjComponent.Vertex vertex : face.vertices){
                triangles.add(obj.pos, vertex.pos*3, 3);
                if(includeTexture) triangles.add(obj.tex, vertex.tex*2, 2);
                if(includeNormal) triangles.add(obj.norm, vertex.norm*3, 3);
            }
        }
        for(ObjComponent subObj : obj.subObj){
            extractTriangles(subObj, triangles, includeTexture, includeNormal);
        }
    }

    private static float[] assembleVertexBuffer(ObjComponent obj, Map<ObjComponent.Vertex, Integer> vertexSet, boolean includeTexture, boolean includeNormals){
        int len = 3 + (includeTexture ? 2 : 0) + (includeNormals ? 3 : 0);
        float[] vertexBuffer = new float[vertexSet.size() * len];

        for (Map.Entry<ObjComponent.Vertex, Integer> entry : vertexSet.entrySet()) {
            int i = entry.getValue() * len;
            ObjComponent.Vertex v = entry.getKey();

            System.arraycopy(obj.pos, v.pos * 3, vertexBuffer, i, 3);
            i += 3;

            if (includeTexture) {
                vertexBuffer[i] = obj.tex[v.tex*2];
                vertexBuffer[i+1] = 1-obj.tex[v.tex*2+1];
                //System.arraycopy(obj.tex, v.tex * 2, vertexBuffer, i, 2);
                i += 2;
            }

            if (includeNormals) {
                System.arraycopy(obj.norm, v.norm * 3, vertexBuffer, i, 3);
            }
        }
        return vertexBuffer;
    }
    private static void extractVerticesAndIndexes(Map<ObjComponent.Vertex, Integer> vertexSet, IntAccum indexBuffer, ObjComponent obj){
        for(ObjComponent.Surface face : obj.faces){
            if(face.vertices.length!=3) continue;
            for(ObjComponent.Vertex v : face.vertices){
                vertexSet.putIfAbsent(v, vertexSet.size());
                indexBuffer.add(vertexSet.get(v));
            }
        }
        for(ObjComponent subObj : obj.subObj){
            extractVerticesAndIndexes(vertexSet, indexBuffer, subObj);
        }
    }
}