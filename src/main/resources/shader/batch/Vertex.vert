#version 330

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aUv;

out vec3 vColor;
out vec2 vUv;

uniform mat4 projection;

void main()
{
    gl_Position = projection * vec4(aPosition, 0.0, 1.0);

    vColor = aColor;
    vUv = aUv;
}
