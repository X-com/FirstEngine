#version 330 core

layout(location=0) in vec4 position;//clip space coords (-1, 1) x (-1, 1)

out vec2 v_screenpos;

void main() {
    gl_Position = position;
    v_screenpos = position.xy;
}
