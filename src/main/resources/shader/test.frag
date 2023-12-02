#version 330

uniform sampler2D tex;

out vec4 color;
in vec2 v_texcoord;

void main() {
    color = texture(tex, v_texcoord);
}