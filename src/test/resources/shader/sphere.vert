#version 330 core

layout(location=0) in vec4 pos;
layout(location=1) in vec3 color;

uniform float u_r;
uniform mat4 u_mvp;

out vec3 v_color;

vec2 sign_not_zero(vec2 v)
{
    return vec2((v.x >= 0.f) ? 1.f : -1.f, (v.y >= 0.f) ? 1.f : -1.f);
}

vec3 oct_decode_dir(vec2 v){
    vec3 n = vec3(v.xy, 1.f - abs(v.x) - abs(v.y));
    if (n.z < 0.f) n.xy = (1.f - abs(n.yx)) * sign_not_zero(n.xy);
    //return normalize(n);
    return n;
}

vec2 oct_encode_dir(vec3 n)
{
    vec2 p = n.xy * (1.f / (abs(n.x) + abs(n.y) + abs(n.z)));
    return (n.z <= 0.f) ? ( (1.f - abs(p.yx)) * sign_not_zero(p) ) : p;
}

void main(){
    v_color = color;
    //vec4 spherepos = vec4(oct_decode_dir(pos.xy)*u_r, 1);
    vec4 spherepos = vec4(oct_decode_dir(pos.xy), 1);
    gl_Position = u_mvp*spherepos;
}