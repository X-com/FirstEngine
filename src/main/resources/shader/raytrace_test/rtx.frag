#version 330 core

uniform mat4 u_proj;
uniform mat4 u_view;
uniform vec3 u_eye;
uniform int u_numTriangles;

uniform sampler2D texture;//sample with uv coordinates from rayhits
uniform samplerBuffer triangles;//geometry for model (x, y, z, u, v, nx, ny, nz)


in vec2 v_screenpos;

out vec4 color;

vec3 fetchPos(int i){
    return texelFetch(triangles, i*2).xyz;
}

vec2 fetchTex(int i){
    return vec2(texelFetch(triangles, i*2).w, texelFetch(triangles, i*2+1).x);
}

vec3 fetchNorm(int i){
    return texelFetch(triangles, i*2+1).yzw;
}

vec3 sampleAt(int i, vec3 o, vec3 d){
    vec3 v0 = fetchPos(i*3);
    vec3 v1 = fetchPos(i*3+1);
    vec3 v2 = fetchPos(i*3+2);

    vec3 e1 = v1-v0;
    vec3 e2 = v2-v0;
    vec3 n = cross(e1, e2);

    float invDet = -1/dot(n, d);

    vec3 tv = o-v0;

    float t = dot(n, tv)*invDet;

    vec3 l = cross(d, tv);

    float u = -dot(l, e2)*invDet;
    float v = dot(l, e1)*invDet;

    if(u+v>1){
        u = 1-u;
        v = 1-v;
    }

    vec2 uv0 = fetchTex(i*3);
    vec2 uv1 = fetchTex(i*3+1);
    vec2 uv2 = fetchTex(i*3+2);

    vec2 texUv = uv0*(1-u-v) + u*uv1 + v*uv2;
    texUv.y = 1-texUv.y;

    vec3 albedo = texture2D(texture, texUv).xyz;

    vec3 n0 = fetchNorm(i*3);
    vec3 n1 = fetchNorm(i*3+1);
    vec3 n2 = fetchNorm(i*3+2);
    vec3 norm = n0*(1-u-v) + u*n1 + v*n2;
    vec3 lightDir = normalize(vec3(-1, -1, -1));
    return albedo*max(-dot(norm, lightDir), 0.3);
}

vec3 raycast(vec3 o, vec3 d){
    int min = -1;
    float minT = 1e20;
    float minDet2 = 0;

    for(int i = 0; i < u_numTriangles; i++){
        vec3 v0 = fetchPos(i*3);
        vec3 v1 = fetchPos(i*3+1);
        vec3 v2 = fetchPos(i*3+2);

        vec3 e1 = v1-v0;
        vec3 e2 = v2-v0;
        vec3 n = cross(e1, e2);

        float det = -dot(n, d);
        float det2 = det*det;

        vec3 tv = o-v0;
        float t = dot(n, tv)*det;
        if(t<0 || minDet2*t > det2*minT){
            continue;
        }
        vec3 l = cross(d, tv);
        float u = -dot(l, e2)*det;
        float v = dot(l, e1)*det;
        if(0<u && 0<v && v+u<det2){
            min = i;
            minT = t;
            minDet2 = det2;
        }
    }
    if(min==-1){
        return vec3(0);
    }
    //return vec3(1);
    return sampleAt(min, o, d);

}

vec3 scale(vec4 v){
    return v.xyz/v.w;
}

void main() {
    vec3 o = u_eye;
    mat4 invProj = inverse(u_proj);
    mat4 invView = inverse(u_view);

    vec3 d = normalize((invView*(invProj*vec4(v_screenpos, 0, 0) + vec4(0, 0, -1, 0))).xyz);
    color = vec4(raycast(o, d), 1);
}
