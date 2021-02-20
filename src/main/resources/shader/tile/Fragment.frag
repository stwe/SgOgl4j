#version 330

out vec4 fragColor;

in vec2 vUv;

uniform float hasTexture;
uniform vec4 color;
uniform sampler2D diffuseMap;

void main()
{
    fragColor = color;
    if (hasTexture > 0.5) {
        fragColor = texture(diffuseMap, vUv);
    }
}
