package file;

import java.util.HashMap;
import java.util.Map;

public class ObjComponent {
    public final float[] pos, tex, norm;
    public final Surface[] faces;
    public final String name;
    public final ObjComponent[] subObj;

    public ObjComponent(Surface[] faces, float[] pos, float[] tex, float[] norm, String name, ObjComponent[] subObj){
        this.faces = faces;
        this.pos = pos;
        this.tex = tex;
        this.norm = norm;
        this.name = name;
        this.subObj = subObj;
    }

    protected ObjComponent(ObjLoader.ObjComponentData data, float[] pos, float[] tex, float[] norm){
        this.pos = pos;
        this.tex = tex;
        this.norm = norm;
        this.name = data.name;
        this.faces = data.faces.toArray(new Surface[0]);
        subObj = new ObjComponent[data.subObj.size()];
        for (int i = 0; i < subObj.length; i++) {
            ObjLoader.ObjComponentData subData = data.subObj.get(i);
            subObj[i] = new ObjComponent(subData, pos, tex, norm);
        }
    }

    //extracts this obj component and its subcomponents from their context
    public ObjComponent extract(){
        Map<Integer, Integer> posMap = new HashMap<>(),
                texMap = new HashMap<>(),
                normMap = new HashMap<>();
        extractUsed(this, posMap, texMap, normMap);

        float[] newPos = copyToArray(pos, posMap, 3),
                newTex = copyToArray(tex, texMap, 2),
                newNorm = copyToArray(norm, normMap, 3);

        return remap(this, posMap, texMap, normMap, newPos, newTex, newNorm);
    }

    private ObjComponent remap(ObjComponent comp, Map<Integer,
            Integer> posMap, Map<Integer, Integer> texMap, Map<Integer, Integer> normMap,
                               float[] pos, float[] tex, float[] norm){
        Surface[] newFaces = new Surface[comp.faces.length];
        for (int i = 0; i < newFaces.length; i++) {
            Surface face = comp.faces[i];
            Vertex[] newVertices = new Vertex[face.vertices.length];
            for (int j = 0; j < newVertices.length; j++) {
                Vertex oldVertex = face.vertices[j];
                newVertices[j] = new Vertex(posMap.get(oldVertex.pos), texMap.get(oldVertex.tex), normMap.get(oldVertex.norm));
            }
            newFaces[i] = new Surface(newVertices);
        }
        ObjComponent[] newSubObj = new ObjComponent[comp.subObj.length];
        for (int i = 0; i < newSubObj.length; i++) {
            newSubObj[i] = remap(comp.subObj[i], posMap, texMap, normMap, pos, tex, norm);
        }

        return new ObjComponent(newFaces, pos, tex, norm, comp.name, newSubObj);
    }

    private float[] copyToArray(float[] old, Map<Integer, Integer> indexMap, int len){
        float[] newAr = new float[indexMap.size()*len];
        for(Map.Entry<Integer, Integer> i : indexMap.entrySet()){
            System.arraycopy(old, i.getKey()*len, newAr, i.getValue()*len, len);
        }
        return newAr;
    }

    private void extractUsed(ObjComponent comp, Map<Integer, Integer> posMap, Map<Integer, Integer> texMap, Map<Integer, Integer> normMap){
        for(Surface surface : comp.faces){
            for(Vertex vertex : surface.vertices){
                posMap.putIfAbsent(vertex.pos, posMap.size());
                texMap.putIfAbsent(vertex.tex, texMap.size());
                normMap.putIfAbsent(vertex.norm, normMap.size());
            }
        }

        for(ObjComponent subComp : comp.subObj){
            extractUsed(subComp, posMap, texMap, normMap);
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

        public Vertex(){
            this(0, 0, 0);
        }

        public Vertex(int pos, int tex, int norm){
            this.pos = pos;
            this.tex = tex;
            this.norm = norm;
        }

        @Override
        public boolean equals(Object b){
            if(!(b instanceof Vertex))
                return false;
            Vertex v = (Vertex) b;
            return pos==v.pos && tex==v.tex && norm==v.norm;
        }
    }

}
