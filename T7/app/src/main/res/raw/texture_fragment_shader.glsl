precision mediump float;
uniform sampler2D u_TextureUnit1; // First texture
uniform sampler2D u_TextureUnit2; // Second texture

varying vec2 v_TextureCoordinates;

void main(){
    vec4 texture1 = texture2D(u_TextureUnit1, v_TextureCoordinates);
    vec4 texture2 = texture2D(u_TextureUnit2, v_TextureCoordinates);
    gl_FragColor = texture1 * 0.7 + texture2 * 0.3;
}
