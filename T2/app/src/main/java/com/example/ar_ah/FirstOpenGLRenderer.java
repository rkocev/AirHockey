package com.example.ar_ah;


import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glViewport;

import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FirstOpenGLRenderer implements Renderer {
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height){
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 glUnused){
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
