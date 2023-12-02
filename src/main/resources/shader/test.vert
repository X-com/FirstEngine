#version 330

uniform mat4 u_mvp;

layout(location=0) in vec4 position;

void main() {
    gl_Position = u_mvp * position;
}