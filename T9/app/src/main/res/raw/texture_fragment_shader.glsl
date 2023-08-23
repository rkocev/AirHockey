precision mediump float;
uniform sampler2D u_TextureUnit;
uniform sampler2D u_TextureUnit2; // This is the mask texture

varying vec2 v_TextureCoordinates;

void main(){
    // Get the color from the air_hockey_surface texture
    vec4 texture1 = texture2D(u_TextureUnit, v_TextureCoordinates);

    // Get the corresponding pixel from the leaves mask texture
    vec4 texture2 = texture2D(u_TextureUnit2, v_TextureCoordinates);

    // Use the inverse of the alpha value of the mask as the final alpha value for the output
    gl_FragColor = vec4(texture1.rgb, 1.0 - texture2.a);
}
