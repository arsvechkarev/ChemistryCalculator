package com.chemistry.calculator.features.camera

import android.graphics.Rect
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chemistry.calculator.core.async.AndroidThreader
import com.chemistry.calculator.views.BoxView

class CameraScreen(
  private var activity: AppCompatActivity?,
  private var boxView: BoxView?,
  private var previewView: TextureView?,
  private var processImageButton: View?,
  private var cropRectProvider: (() -> Rect)?,
  private var onStringReady: ((String) -> Unit)?
) {
  
  private val openGlCameraRenderer = CameraRenderer(activity!!, onPreviewStarted = {
    AndroidThreader.onMainThread { boxView?.animateAppearance() }
  })
  
  fun startCamera() {
    previewView!!.surfaceTextureListener = openGlCameraRenderer
  }
  
  fun release() {
    activity = null
    boxView = null
    previewView = null
    processImageButton = null
    cropRectProvider = null
    onStringReady = null
  }
}