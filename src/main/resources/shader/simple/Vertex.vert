#version 330

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aColor;

out vec3 vColor;

uniform mat4 p;
uniform mat4 v;
uniform mat4 m;

void main()
{
    gl_Position = p * v * m * vec4(aPosition, 1.0);
    vColor = aColor;
}
