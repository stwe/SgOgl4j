#version 330

out vec4 fragColor;

in vec3 vColor;
in vec2 vUv;

uniform float hasDiffuseMap;
uniform sampler2D diffuseMap;

void main()
{
    fragColor = vec4(vColor, 1.0);
    if (hasDiffuseMap > 0.5) {
        fragColor = texture(diffuseMap, vUv);
    }
}
