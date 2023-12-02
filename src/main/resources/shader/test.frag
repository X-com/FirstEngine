#version 330

uniform sampler2D tex;

out vec4 color;
//in vec2 v_texcoord;
in float mul;

void main() {
    //color = texture(tex, v_texcoord);
    color = vec4(mul);
}