#version 330

uniform mat4 u_mvp;

layout(location=0) in vec4 position;
layout(location=1) in vec3 normal;

//layout(location=1) in vec2 texcoord;

//out vec2 v_texcoord;

out float mul;

void main() {
    gl_Position = u_mvp * position;
    vec3 lightDir = vec3(-1, -1, -1);
    lightDir = normalize(lightDir);
    mul = (max(dot(normal, lightDir), 0)+0.2)/1.2;
}