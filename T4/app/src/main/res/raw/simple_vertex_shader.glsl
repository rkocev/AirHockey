attribute vec4 a_Position;
attribute vec4 a_Color;
uniform mat4 u_Matrix;

varying vec4 v_Color;

void main() {
    v_Color = a_Color;
    gl_PointSize = 20.0;
    gl_Position = u_Matrix * a_Position;
}