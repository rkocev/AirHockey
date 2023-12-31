package com.example.AirHockey.utils;

import static android.opengl.GLES20.*;

import android.util.Log;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode){
        final int shaderObjectID = glCreateShader(type);
        if(shaderObjectID == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "Could not create new shader.");
            }
            return 0;
        }

        // Pass in the shader source.
        glShaderSource(shaderObjectID, shaderCode);

        // Compile the shader.
        glCompileShader(shaderObjectID);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectID, GL_COMPILE_STATUS, compileStatus, 0);

        if(compileStatus[0] == 0){
            // If it failed, delete the shader object.
            glDeleteShader(shaderObjectID);
            if(LoggerConfig.ON){
                Log.w(TAG, "Compilation of shader failed.");
            }
            return 0;
        }
        return shaderObjectID;
    }

    public static int linkProgram(int vertexShader, int fragmentShader){
        int programObjectId = glCreateProgram();
        if(programObjectId == 0){
            if(LoggerConfig.ON){
                Log.e(TAG, "Could not creat new program.");
            }
            return 0;
        }

        glAttachShader(programObjectId, vertexShader);
        glAttachShader(programObjectId, fragmentShader);
        glLinkProgram(programObjectId);

        // Get the link status.
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (LoggerConfig.ON) {
            // Print the program info log to the Android log output.
            Log.v(TAG, "Results of linking program:\n"
                    + glGetProgramInfoLog(programObjectId));
        }

        // Verify the link status.
        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed.");
            }
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0] +
                "\nLog: " + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
