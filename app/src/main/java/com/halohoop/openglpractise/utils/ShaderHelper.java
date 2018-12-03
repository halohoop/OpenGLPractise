package com.halohoop.openglpractise.utils;

//import

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            Utils.logi("Could not create new shader.");
            return 0;
        }
        //把着色器代码上川岛着色器对象里
        glShaderSource(shaderObjectId, shaderCode);
        // 编译这个着色器
        glCompileShader(shaderObjectId);

        //取出编译的状态
        final int compileStatus[] = new int[1];
        //最后一个参数是指写入到第0个里面
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        String log = glGetShaderInfoLog(shaderObjectId);
        Utils.logi(log);

        if (compileStatus[0] == 0) {
            //有问题
            glDeleteShader(shaderObjectId);
            Utils.logi("Compilation of shader failed.");

            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();

        if (programObjectId == 0) {
            Utils.logi("Could not create new program.");
            return 0;
        }

        //附加到程序对象上（程序对象->OpenGL的一个组件）
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            Utils.logi("Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Utils.logi("Results of validating program: " + validateStatus[0]
                + "\nLog: " + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
