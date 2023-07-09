uniform float u_PointSize;
attribute vec4 a_Position;

void main() {
    gl_PointSize = u_PointSize;
    gl_Position = a_Position;
}