#version 430
#extension GL_ARB_bindless_texture : require
#extension GL_ARB_gpu_shader5 : require
#extension GL_ARB_gpu_shader_int64 : require

out vec4 fragColor;

in vec2 vUv;

uniform int index;

uniform maps {
    sampler2D handles[2];
};

void main()
{
    fragColor = texture(handles[index], vUv);
}
