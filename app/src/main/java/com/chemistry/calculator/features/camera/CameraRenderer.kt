package com.chemistry.calculator.features.camera

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Pair
import android.view.TextureView
import com.chemistry.calculator.features.camera.MyGLUtils.genTexture
import timber.log.Timber
import java.io.IOException
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface

class CameraRenderer(
  private var onPreviewStarted: (() -> Unit)? = null
) : TextureView.SurfaceTextureListener, Runnable {
  
  private var renderThread: Thread? = null
  private var surfaceTexture: SurfaceTexture? = null
  private var gwidth = 0
  private var gheight = 0
  
  private var eglDisplay: EGLDisplay? = null
  private var eglSurface: EGLSurface? = null
  private var eglContext: EGLContext? = null
  private var egl10: EGL10? = null
  
  private var camera: Camera? = null
  private var cameraSurfaceTexture: SurfaceTexture? = null
  private var cameraTextureId = 0
  private var cameraFilter: CameraFilter? = null
  
  override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
  
  override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    gwidth = -width
    gheight = -height
  }
  
  override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
    if (camera != null) {
      camera!!.stopPreview()
      camera!!.release()
    }
    if (renderThread != null && renderThread!!.isAlive) {
      renderThread!!.interrupt()
    }
    cameraFilter!!.release()
    onPreviewStarted = null
    return true
  }
  
  override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
    if (renderThread != null && renderThread!!.isAlive) {
      renderThread!!.interrupt()
    }
    renderThread = Thread(this)
    
    surfaceTexture = surface
    gwidth = -width
    gheight = -height
    
    // Open camera
    val backCamera = getBackCamera()!!
    val backCameraId = backCamera.second
    camera = Camera.open(backCameraId)
    
    // Start rendering
    renderThread!!.start()
  }
  
  override fun run() {
    initGL(surfaceTexture!!)
    cameraFilter = CameraFilter()
    
    // Create texture for camera preview
    cameraTextureId = genTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
    cameraSurfaceTexture = SurfaceTexture(cameraTextureId)
  
    onPreviewStarted?.invoke()
  
    // Start camera preview
    try {
      camera!!.setPreviewTexture(cameraSurfaceTexture)
      camera!!.startPreview()
    } catch (ioe: IOException) {
      // Something bad happened
    }
    
    // Render loop
    while (!Thread.currentThread().isInterrupted) {
      try {
        if (gwidth < 0 && gheight < 0) {
          gwidth = -gwidth
          gheight = -gheight
          GLES20.glViewport(0, 0, gwidth, gheight)
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        
        // Update the camera preview texture
        synchronized(this) { cameraSurfaceTexture!!.updateTexImage() }
        
        // Draw camera preview
        cameraFilter!!.draw(cameraTextureId, gwidth, gheight)
        
        // Flush
        GLES20.glFlush()
        egl10!!.eglSwapBuffers(eglDisplay, eglSurface)
        Thread.sleep(DRAW_INTERVAL.toLong())
      } catch (e: InterruptedException) {
        Thread.currentThread().interrupt()
      }
    }
    
    cameraSurfaceTexture!!.release()
    GLES20.glDeleteTextures(1, intArrayOf(cameraTextureId), 0)
  }
  
  private fun initGL(texture: SurfaceTexture) {
    egl10 = EGLContext.getEGL() as EGL10
    
    eglDisplay = egl10!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
    if (eglDisplay === EGL10.EGL_NO_DISPLAY) {
      throw RuntimeException("eglGetDisplay failed " +
          GLUtils.getEGLErrorString(egl10!!.eglGetError()))
    }
    
    val version = IntArray(2)
    if (!egl10!!.eglInitialize(eglDisplay, version)) {
      throw java.lang.RuntimeException(
        "eglInitialize failed " + GLUtils.getEGLErrorString(egl10!!.eglGetError()))
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
    
    var eglConfig: EGLConfig? = null
    require(egl10!!.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)) {
      "eglChooseConfig failed " + GLUtils.getEGLErrorString(egl10!!.eglGetError())
    }
    if (configsCount[0] > 0) {
      eglConfig = configs[0]
    }
    if (eglConfig == null) {
      throw java.lang.RuntimeException("eglConfig not initialized")
    }
    
    val attrib_list = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2,
      EGL10.EGL_NONE)
    eglContext = egl10!!
        .eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list)
    eglSurface = egl10!!.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null)
    
    if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
      val error = egl10!!.eglGetError()
      if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
        Timber.e("eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW")
        return
      }
      throw java.lang.RuntimeException("eglCreateWindowSurface failed " +
          GLUtils.getEGLErrorString(error))
    }
    
    if (!egl10!!.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
      throw java.lang.RuntimeException("eglMakeCurrent failed " +
          GLUtils.getEGLErrorString(egl10!!.eglGetError()))
    }
  }
  
  private fun getBackCamera(): Pair<Camera.CameraInfo, Int>? {
    val cameraInfo = Camera.CameraInfo()
    val numberOfCameras = Camera.getNumberOfCameras()
    
    for (i in 0 until numberOfCameras) {
      Camera.getCameraInfo(i, cameraInfo)
      if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
        return Pair(cameraInfo, i)
      }
    }
    return null
  }
  
  companion object {
    private const val EGL_OPEN_GL_ES2_BIT = 4
    private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
    private const val DRAW_INTERVAL = 1000 / 30
  }
}