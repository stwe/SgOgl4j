#version 330

out vec4 fragColor;

uniform float test;

void main()
{
    fragColor = vec4(vec3(test, test, test), 1.0);
}
