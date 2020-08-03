package com.chemistry.calculator.features.camera

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.chemistry.calculator.features.camera.Shaders.FRAGMENT_SHADER
import com.chemistry.calculator.features.camera.Shaders.FRAGMENT_SHADER_RTT
import com.chemistry.calculator.features.camera.Shaders.VERTEX_SHADER
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CameraFilter {
  
  companion object {
    
    private val SQUARE_COORDINATES = floatArrayOf(
      1.0f, -1.0f,
      -1.0f, -1.0f,
      1.0f, 1.0f,
      -1.0f, 1.0f
    )
    
    private val TEXTURE_COORDINATES = floatArrayOf(
      1.0f, 0.0f,
      0.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 1.0f
    )
    
    private val rotatedTextureCoordinates = floatArrayOf(
      1.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 0.0f,
      0.0f, 1.0f
    )
    
    private const val I_CHANNEL_0 = "iChannel0"
    private const val V_POSITION = "vPosition"
    private const val V_TEX_COORDINATE = "vTexCoord"
    private const val I_RESOLUTION = "iResolution"
    private const val I_GLOBAL_TIME = "iGlobalTime"
    private const val I_FRAME = "iFrame"
  }
  
  private val program = MyGLUtils.buildProgram(VERTEX_SHADER, FRAGMENT_SHADER)
  private val startTime = System.currentTimeMillis()
  
  private var cameraRenderBuffer: RenderBuffer? = null
  
  private val rotatedTextureCoordinatesBuffer: FloatBuffer
  
  private var PROGRAM = MyGLUtils.buildProgram(VERTEX_SHADER, FRAGMENT_SHADER_RTT)
  
  
  private val vertexBuffer: FloatBuffer
  private val textureCoordinatesBuffer: FloatBuffer
  
  
  private var iFrame = 0
  
  init {
    vertexBuffer = ByteBuffer.allocateDirect(SQUARE_COORDINATES.size * 4)
        .order(ByteOrder.nativeOrder()).asFloatBuffer()
    vertexBuffer.put(SQUARE_COORDINATES).flip()
    
    textureCoordinatesBuffer = ByteBuffer.allocateDirect(TEXTURE_COORDINATES.size * 4)
        .order(ByteOrder.nativeOrder()).asFloatBuffer()
    textureCoordinatesBuffer.put(TEXTURE_COORDINATES).flip()
    
    rotatedTextureCoordinatesBuffer = ByteBuffer.allocateDirect(rotatedTextureCoordinates.size * 4)
        .order(ByteOrder.nativeOrder()).asFloatBuffer()
    rotatedTextureCoordinatesBuffer.put(rotatedTextureCoordinates).flip()
  }
  
  fun draw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
    // Create camera render buffer
    if (cameraRenderBuffer == null || cameraRenderBuffer!!.width != canvasWidth || cameraRenderBuffer!!.height != canvasHeight) {
      cameraRenderBuffer = RenderBuffer(canvasWidth, canvasHeight, GLES20.GL_TEXTURE8)
    }
    
    // Use shaders
    GLES20.glUseProgram(PROGRAM)
    
    val iChannel0Location = GLES20.glGetUniformLocation(PROGRAM, I_CHANNEL_0)
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexId)
    GLES20.glUniform1i(iChannel0Location, 0)
    
    val vPositionLocation = GLES20.glGetAttribLocation(PROGRAM, V_POSITION)
    GLES20.glEnableVertexAttribArray(vPositionLocation)
    GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2,
      vertexBuffer)
    
    val vTexCoordLocation = GLES20.glGetAttribLocation(PROGRAM, V_TEX_COORDINATE)
    GLES20.glEnableVertexAttribArray(vTexCoordLocation)
    GLES20.glVertexAttribPointer(vTexCoordLocation, 2, GLES20.GL_FLOAT, false, 4 * 2,
      rotatedTextureCoordinatesBuffer)
    
    // Render to texture
    cameraRenderBuffer!!.bind()
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    cameraRenderBuffer!!.unbind()
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    onDraw(cameraRenderBuffer!!.texId, canvasWidth, canvasHeight)
    iFrame++
  }
  
  fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
    setupShaderInputs(program, vertexBuffer, textureCoordinatesBuffer,
      intArrayOf(canvasWidth, canvasHeight), cameraTexId)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
  }
  
  fun setupShaderInputs(program: Int, vertex: FloatBuffer?, textureCoord: FloatBuffer?,
                        iResolution: IntArray, iChannel: Int) {
    GLES20.glUseProgram(program)
    val iResolutionLocation = GLES20.glGetUniformLocation(program, I_RESOLUTION)
    GLES20.glUniform3fv(iResolutionLocation, 1,
      FloatBuffer.wrap(floatArrayOf(iResolution[0].toFloat(), iResolution[1].toFloat(), 1.0f)))
    val time = (System.currentTimeMillis() - startTime).toFloat() / 1000.0f
    val iGlobalTimeLocation = GLES20.glGetUniformLocation(program, I_GLOBAL_TIME)
    GLES20.glUniform1f(iGlobalTimeLocation, time)
    val iFrameLocation = GLES20.glGetUniformLocation(program, I_FRAME)
    GLES20.glUniform1i(iFrameLocation, iFrame)
    val vPositionLocation = GLES20.glGetAttribLocation(program, V_POSITION)
    GLES20.glEnableVertexAttribArray(vPositionLocation)
    GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, vertex)
    val vTexCoordLocation = GLES20.glGetAttribLocation(program, V_TEX_COORDINATE)
    GLES20.glEnableVertexAttribArray(vTexCoordLocation)
    GLES20.glVertexAttribPointer(vTexCoordLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, textureCoord)
    val sTextureLocation = GLES20.glGetUniformLocation(program, I_CHANNEL_0)
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iChannel)
    GLES20.glUniform1i(sTextureLocation, 0)
  }
  
  fun release() {
    PROGRAM = 0
    cameraRenderBuffer = null
  }
}