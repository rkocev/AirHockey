attribute vec4 a_Position;

void main() {
    gl_PointSize = 30.0;
    gl_Position = a_Position;
}