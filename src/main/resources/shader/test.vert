#version 330

uniform mat4 u_mvp;

layout(location=0) in vec4 position;
layout(location=1) in vec2 texcoord;

out vec2 v_texcoord;

void main() {
    gl_Position = u_mvp * position;
    v_texcoord = texcoord;
}