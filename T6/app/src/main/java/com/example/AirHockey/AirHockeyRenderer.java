package com.example.AirHockey;


import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glViewport;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.example.AirHockey.utils.LoggerConfig;
import com.example.AirHockey.utils.MatrixHelper;
import com.example.AirHockey.utils.ShaderHelper;
import com.example.AirHockey.utils.TextResourceReader;
import com.example.ar_ah.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRenderer implements Renderer {
    private final Context context;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;
    private int program;
    private int uMatrixLocation;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] temp = new float[16];

    public AirHockeyRenderer(Context context) {
        this.context = context;

        float[] tableVerticesWithTriangles = {
                // Order of coordinates: X, Y, Z, W, R, G, B
                // Triangle Fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
        };


        vertexData = ByteBuffer.allocateDirect(
                tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(
                context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(
                context, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, "a_Color");
        aPositionLocation = glGetAttribLocation(program, "a_Position");
        uMatrixLocation = glGetUniformLocation(program, "u_Matrix");

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);

    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     *
     * @param width  The new width, in pixels.
     * @param height The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width/(float)height, 1f, 10f);
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0, 0f, 0f, -4.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        rotateM(modelMatrix, 0, -90f, 0f, 0f, 1f);

        multiplyMM(temp,0 ,projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp,0, projectionMatrix,0, temp.length);

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Triangle fan.
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // Dividing line.
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet blue.
        glDrawArrays(GL_POINTS, 8, 1);

        // Draw the second mallet red.
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
