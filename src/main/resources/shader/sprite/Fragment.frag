#version 330

out vec4 fragColor;

in vec2 vUv;

uniform sampler2D diffuseMap;
uniform vec3 color;

void main()
{
    fragColor = vec4(color, 1.0) * texture(diffuseMap, vUv);
}
