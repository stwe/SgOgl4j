#version 430

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aUv;

out vec2 vUv;

layout (std140) uniform matrices
{
    mat4 projection;
    mat4 model;
};

void main()
{
    gl_Position = projection * model * vec4(aPosition, 0.0, 1.0);
    vUv = aUv;
}
