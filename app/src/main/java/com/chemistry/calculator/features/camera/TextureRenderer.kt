package com.chemistry.calculator.features.camera

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLUtils
import android.view.TextureView
import timber.log.Timber
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface

@Suppress("DEPRECATION")
class TextureRenderer(
  private var onPreviewStarted: (() -> Unit)? = null
) : TextureView.SurfaceTextureListener, Runnable {
  
  private lateinit var camera: Camera
  private lateinit var openGLCameraDrawer: OpenGLCameraDrawer
  private lateinit var cameraSurfaceTexture: SurfaceTexture
  
  private lateinit var renderThread: Thread
  private lateinit var surfaceTexture: SurfaceTexture
  private lateinit var eglDisplay: EGLDisplay
  private lateinit var eglContext: EGLContext
  private lateinit var egl10: EGL10
  private var eglSurface: EGLSurface? = null
  
  private var cameraTextureId = 0
  private var glWidth = 0
  private var glHeight = 0
  
  fun onClick() {
    // TODO (8/5/2020): Autofocus on click
  }
  
  override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
  
  override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    glWidth = -width
    glHeight = -height
  }
  
  override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
    camera.stopPreview()
    camera.release()
    if (renderThread.isAlive) {
      renderThread.interrupt()
    }
    openGLCameraDrawer.release()
    onPreviewStarted = null
    return true
  }
  
  override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
    renderThread = Thread(this)
    
    surfaceTexture = surface
    glWidth = -width
    glHeight = -height
    
    camera = Camera.open().apply {
      val params = parameters
      params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
      parameters = params
    }
    renderThread.start()
  }
  
  override fun run() {
    initGL()
    openGLCameraDrawer = OpenGLCameraDrawer()
    cameraTextureId = GLHelper.generateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
    cameraSurfaceTexture = SurfaceTexture(cameraTextureId)
    
    onPreviewStarted?.invoke()
    
    camera.setPreviewTexture(cameraSurfaceTexture)
    camera.startPreview()
    
    // Render loop
    while (!Thread.currentThread().isInterrupted) {
      try {
        if (glWidth < 0 && glHeight < 0) {
          glWidth = -glWidth
          glHeight = -glHeight
          GLES20.glViewport(0, 0, glWidth, glHeight)
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        
        // Update the camera preview texture
        synchronized(this) { cameraSurfaceTexture.updateTexImage() }
        
        // Draw camera preview
        openGLCameraDrawer.draw(cameraTextureId, glWidth, glHeight)
        
        // Flush
        GLES20.glFlush()
        egl10.eglSwapBuffers(eglDisplay, eglSurface)
        Thread.sleep(DRAW_INTERVAL)
      } catch (e: InterruptedException) {
        // Might happen when user finishes activity and destroys surface texture
        Thread.currentThread().interrupt()
      }
    }
    
    cameraSurfaceTexture.release()
    GLES20.glDeleteTextures(1, intArrayOf(cameraTextureId), 0)
  }
  
  private fun initGL() {
    egl10 = EGLContext.getEGL() as EGL10
    
    eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
    require(eglDisplay !== EGL10.EGL_NO_DISPLAY) {
      "eglGetDisplay failed ${GLUtils.getEGLErrorString(egl10.eglGetError())}"
    }
    
    val version = IntArray(2)
    require(egl10.eglInitialize(eglDisplay, version)) {
      "eglInitialize failed ${GLUtils.getEGLErrorString(egl10.eglGetError())}"
    }
    
    val configsCount = IntArray(1)
    val configs = arrayOfNulls<EGLConfig>(1)
    val configSpec = intArrayOf(
      EGL10.EGL_RENDERABLE_TYPE,
      EGL_OPEN_GL_ES2_BIT,
      EGL10.EGL_RED_SIZE, 8,
      EGL10.EGL_GREEN_SIZE, 8,
      EGL10.EGL_BLUE_SIZE, 8,
      EGL10.EGL_ALPHA_SIZE, 8,
      EGL10.EGL_DEPTH_SIZE, 0,
      EGL10.EGL_STENCIL_SIZE, 0,
      EGL10.EGL_NONE
    )
    
    val eglConfig: EGLConfig
    val chooseConflict = egl10.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)
    require(chooseConflict) { "eglChooseConfig failed: ${GLUtils.getEGLErrorString(egl10.eglGetError())}" }
    require(configsCount[0] > 0) { "eglConfig not initialized" }
    eglConfig = configs[0]!!
    
    val attributesList = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
    eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attributesList)
    eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null)
    if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
      val error = egl10.eglGetError()
      if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
        Timber.e("eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW")
        return
      }
      error("eglCreateWindowSurface failed ${GLUtils.getEGLErrorString(error)}")
    }
    
    require(egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
      "eglMakeCurrent failed ${GLUtils.getEGLErrorString(egl10.eglGetError())}"
    }
  }
  
  companion object {
    private const val EGL_OPEN_GL_ES2_BIT = 4
    private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
    private const val DRAW_INTERVAL = 1000L / 30
  }
}