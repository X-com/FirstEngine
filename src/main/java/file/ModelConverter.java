package file;

import opengl.IndexBuffer;
import opengl.VertexArray;
import opengl.VertexBuffer;
import opengl.VertexBufferLayout;

import java.util.*;

public class ModelConverter {
    public static VertexArray extractFromObjs(ObjLoader.Obj obj, boolean includeTexture, boolean includeNormals){
        //put all vertices into a hashmap
        Map<ObjLoader.Vertex, Integer> vertexSet = new HashMap();
        int vertIndex = 0;
        FloatAccum posTemp = new FloatAccum(),
                texTemp = new FloatAccum(),
                normTemp = new FloatAccum();

        posTemp.add(obj.pos);
        texTemp.add(obj.tex);
        normTemp.add(obj.norm);

        for(ObjLoader.Surface surface : obj.surfaces){
            for(ObjLoader.Vertex vertex : surface.vertices){
                vertexSet.put(vertex, vertIndex++);//0
            }
        }

        for(ObjLoader.Group group : obj.groups){
            for(ObjLoader.Surface surface : group.surfaces){
                for(ObjLoader.Vertex vertex : surface.vertices){
                    vertexSet.put(vertex, vertIndex++);//1
                }
            }
            posTemp.add(group.pos);
            texTemp.add(group.tex);
            normTemp.add(group.norm);
        }

        for(ObjLoader.SubObj subObj : obj.subObjs){
            posTemp.add(subObj.pos);
            texTemp.add(subObj.tex);
            normTemp.add(subObj.norm);

            for(ObjLoader.Surface surface : subObj.surfaces){
                for(ObjLoader.Vertex vertex : surface.vertices){
                    vertexSet.put(vertex, vertIndex++);//2
                }
            }

            for(ObjLoader.Group group : subObj.groups){
                posTemp.add(group.pos);
                texTemp.add(group.tex);
                normTemp.add(group.norm);
                for(ObjLoader.Surface surface : group.surfaces){
                    for(ObjLoader.Vertex vertex : surface.vertices){
                        vertexSet.put(vertex, vertIndex++);//3
                    }
                }
            }
        }

        float[] pos = posTemp.getData(),
                tex = texTemp.getData(),
                norm = normTemp.getData();

        int mul = 3 + (includeTexture?2:0) + (includeNormals?3:0);
        float[] vboAr = new float[vertexSet.size()*mul];

        for(Map.Entry<ObjLoader.Vertex, Integer> vertexEntry : vertexSet.entrySet()){
            int i = vertexEntry.getValue();
            ObjLoader.Vertex v = vertexEntry.getKey();

            int j = i*mul;
            vboAr[j++] = pos[v.pos*3];
            vboAr[j++] = pos[v.pos*3+1];
            vboAr[j++] = pos[v.pos*3+2];

            if(includeTexture) {
                vboAr[j++] = tex[v.tex*2];
                vboAr[j++] = 1-tex[v.tex*2 + 1];
            }
            if(includeNormals) {
                vboAr[j++] = norm[v.norm*3];
                vboAr[j++] = norm[v.norm*3+1];
                vboAr[j++] = norm[v.norm*3+2];
            }
        }

        IntAccum tempIbo = new IntAccum();

        for(ObjLoader.Surface surface : obj.surfaces){
            appendSurface(surface, tempIbo, vertexSet);
        }

        for(ObjLoader.Group group : obj.groups){
            for(ObjLoader.Surface surface : group.surfaces){
                appendSurface(surface, tempIbo, vertexSet);
            }
        }

        for(ObjLoader.SubObj subObj : obj.subObjs) {
            for (ObjLoader.Surface surface : subObj.surfaces) {
                appendSurface(surface, tempIbo, vertexSet);
            }

            for (ObjLoader.Group group : subObj.groups) {
                for (ObjLoader.Surface surface : group.surfaces) {
                    appendSurface(surface, tempIbo, vertexSet);
                }
            }
        }


        VertexArray va = new VertexArray();
        VertexBufferLayout layout = new VertexBufferLayout();
        layout.addFloat(3);//pos
        if(includeTexture)
            layout.addFloat(2);//tex
        if(includeNormals)
            layout.addFloat(3);//norm

        VertexBuffer vbo = new VertexBuffer(vboAr);
        IndexBuffer ibo = new IndexBuffer(tempIbo.getData());
        va.addVertexBuffer(vbo, layout);
        va.setIndexBuffer(ibo);

        return va;
    }

    private static void appendSurface(ObjLoader.Surface surface, IntAccum tempIbo, Map<ObjLoader.Vertex, Integer> vertexSet) {
        if(surface.vertices.length==3)
            for(ObjLoader.Vertex vertex : surface.vertices){
                tempIbo.add(vertexSet.get(vertex));
            }
    }
}
