#version 330

uniform mat4 u_mvp;

layout(location=0) in vec4 position;
layout(location=1) in vec2 texcoord;
layout(location=2) in vec3 normal;

out vec2 v_texcoord;
out vec3 v_norm;

void main() {
    gl_Position = u_mvp * position;
    v_texcoord = texcoord;
    v_norm = normal;
}