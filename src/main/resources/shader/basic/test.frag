#version 330

uniform sampler2D tex;

out vec4 color;
in vec2 v_texcoord;
in vec3 v_norm;

void main() {
    vec3 light_dir = normalize(vec3(-1, -1, -1));
    color = texture(tex, v_texcoord)*(max(-dot(v_norm, light_dir), 0.3));
}