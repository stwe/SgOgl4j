#version 430

out vec4 fragColor;

in vec2 vUv;

uniform int index;
uniform sampler2D diffuseMap;

uniform maps {
    vec4 handles[2];
};

void main()
{
    fragColor = texture(diffuseMap, vUv);
    fragColor.x = handles[index].x;
}
