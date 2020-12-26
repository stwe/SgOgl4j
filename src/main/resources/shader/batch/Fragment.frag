#version 330

out vec4 fragColor;

in vec3 vColor;
in vec2 vUv;

uniform sampler2D diffuseMap;

void main()
{
    //fragColor = vec4(vColor, 1.0) * texture(diffuseMap, vUv);
    fragColor = texture(diffuseMap, vUv);
    //fragColor = vec4(vColor, 1.0);
}
