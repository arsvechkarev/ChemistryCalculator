package com.chemistry.calculator.features.camera

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.chemistry.calculator.features.camera.Shaders.FRAGMENT_SHADER
import com.chemistry.calculator.features.camera.Shaders.FRAGMENT_SHADER_RTT
import com.chemistry.calculator.features.camera.Shaders.VERTEX_SHADER
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class OpenGLCameraDrawer {
  
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
  
  private val program = GLHelper.buildProgram(VERTEX_SHADER, FRAGMENT_SHADER)
  private val programRtt = GLHelper.buildProgram(VERTEX_SHADER, FRAGMENT_SHADER_RTT)
  
  private var cameraRenderBuffer: RenderBuffer? = null
  private val startTime = System.currentTimeMillis()
  private var iFrame = 0
  
  private val rotatedTextureCoordinatesBuffer: FloatBuffer
  private val vertexBuffer: FloatBuffer
  private val textureCoordinatesBuffer: FloatBuffer
  private val resolutionBuffer = FloatBuffer.allocate(3)
  
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
  
  fun draw(cameraTextureId: Int, width: Int, height: Int) {
    // Create camera render buffer
    if (cameraRenderBuffer == null
        || cameraRenderBuffer?.width != width || cameraRenderBuffer?.height != height) {
      cameraRenderBuffer = RenderBuffer(width, height, GLES20.GL_TEXTURE8)
    }
    
    // Use shaders
    GLES20.glUseProgram(programRtt)
    
    val iChannel0Location = GLES20.glGetUniformLocation(programRtt, I_CHANNEL_0)
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTextureId)
    GLES20.glUniform1i(iChannel0Location, 0)
    
    val vPositionLocation = GLES20.glGetAttribLocation(programRtt, V_POSITION)
    GLES20.glEnableVertexAttribArray(vPositionLocation)
    GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2,
      vertexBuffer)
    
    val vTexCoordinateLocation = GLES20.glGetAttribLocation(programRtt, V_TEX_COORDINATE)
    GLES20.glEnableVertexAttribArray(vTexCoordinateLocation)
    GLES20.glVertexAttribPointer(vTexCoordinateLocation, 2, GLES20.GL_FLOAT, false, 4 * 2,
      rotatedTextureCoordinatesBuffer)
    
    // Render to texture
    cameraRenderBuffer!!.bind()
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    cameraRenderBuffer!!.unbind()
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    onDraw(width.toFloat(), height.toFloat())
    iFrame++
  }
  
  private fun onDraw(canvasWidth: Float, canvasHeight: Float) {
    setupShaderInputs(program, vertexBuffer, textureCoordinatesBuffer,
      canvasWidth, canvasHeight, cameraRenderBuffer!!.textureId)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
  }
  
  private fun setupShaderInputs(program: Int, vertex: FloatBuffer?, textureCoordinates: FloatBuffer?,
                                width: Float, height: Float, iChannel: Int) {
    GLES20.glUseProgram(program)
    val iResolutionLocation = GLES20.glGetUniformLocation(program, I_RESOLUTION)
    with(resolutionBuffer) {
      clear()
      put(width)
      put(height)
      put(1.0f)
      flip()
    }
    GLES20.glUniform3fv(iResolutionLocation, 1, resolutionBuffer)
    val time = (System.currentTimeMillis() - startTime).toFloat() / 1000.0f
    val iGlobalTimeLocation = GLES20.glGetUniformLocation(program, I_GLOBAL_TIME)
    GLES20.glUniform1f(iGlobalTimeLocation, time)
    val iFrameLocation = GLES20.glGetUniformLocation(program, I_FRAME)
    GLES20.glUniform1i(iFrameLocation, iFrame)
    val vPositionLocation = GLES20.glGetAttribLocation(program, V_POSITION)
    GLES20.glEnableVertexAttribArray(vPositionLocation)
    GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, vertex)
    val vTexCoordinateLocation = GLES20.glGetAttribLocation(program, V_TEX_COORDINATE)
    GLES20.glEnableVertexAttribArray(vTexCoordinateLocation)
    GLES20.glVertexAttribPointer(vTexCoordinateLocation, 2, GLES20.GL_FLOAT, false, 4 * 2,
      textureCoordinates)
    val sTextureLocation = GLES20.glGetUniformLocation(program, I_CHANNEL_0)
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iChannel)
    GLES20.glUniform1i(sTextureLocation, 0)
  }
  
  fun release() {
    cameraRenderBuffer = null
  }
}