package com.chemistry.calculator.features.camera

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import javax.microedition.khronos.opengles.GL10

object MyGLUtils {
  private const val TAG = "MyGLUtils"
  
  @JvmStatic
  @JvmOverloads
  fun genTexture(textureType: Int = GLES20.GL_TEXTURE_2D): Int {
    val genBuf = IntArray(1)
    GLES20.glGenTextures(1, genBuf, 0)
    GLES20.glBindTexture(textureType, genBuf[0])
    
    // Set texture default draw parameters
    if (textureType == GLES11Ext.GL_TEXTURE_EXTERNAL_OES) {
      GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER,
        GL10.GL_LINEAR.toFloat())
      GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER,
        GL10.GL_LINEAR.toFloat())
      GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
      GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
    } else {
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT)
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT)
    }
    return genBuf[0]
  }
  
  fun buildProgram(vertexSource: String?, fragmentSource: String?): Int {
    val vertexShader = buildShader(GLES20.GL_VERTEX_SHADER, vertexSource)
    if (vertexShader == 0) {
      return 0
    }
    val fragmentShader = buildShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
    if (fragmentShader == 0) {
      return 0
    }
    val program = GLES20.glCreateProgram()
    if (program == 0) {
      return 0
    }
    GLES20.glAttachShader(program, vertexShader)
    GLES20.glAttachShader(program, fragmentShader)
    GLES20.glLinkProgram(program)
    return program
  }
  
  fun buildShader(type: Int, shaderSource: String?): Int {
    val shader = GLES20.glCreateShader(type)
    if (shader == 0) {
      return 0
    }
    GLES20.glShaderSource(shader, shaderSource)
    GLES20.glCompileShader(shader)
    val status = IntArray(1)
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
    if (status[0] == 0) {
      Log.e(TAG, GLES20.glGetShaderInfoLog(shader))
      GLES20.glDeleteShader(shader)
      return 0
    }
    return shader
  }
}