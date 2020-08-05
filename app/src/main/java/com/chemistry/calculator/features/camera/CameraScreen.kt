package com.chemistry.calculator.features.camera

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chemistry.calculator.core.async.AndroidThreader
import com.chemistry.calculator.views.BoxView
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock

class CameraScreen(
  private var activity: AppCompatActivity?,
  private var boxView: BoxView?,
  private var previewView: TextureView?,
  private var processImageButton: View?,
  private var onStringReady: ((String) -> Unit)?
) {
  
  private val textRecognizer = CameraScreenDi.provideTextRecognizer(activity!!)
  
  private val openGlCameraRenderer = TextureRenderer(onPreviewStarted = {
    AndroidThreader.onMainThread { boxView?.animateAppearance() }
  })
  
  init {
    boxView!!.setOnClickListener {
      openGlCameraRenderer.onClick()
    }
    textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
      override fun release() {}
      override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
        val items = detections.detectedItems
        if (items.size() != 0) {
          onStringReady?.invoke(items[0].value)
        }
      }
    })
    processImageButton!!.setOnClickListener {
      val bitmap = previewView!!.bitmap.crop(boxView!!.frameBox)
      val frame = Frame.Builder()
          .setBitmap(bitmap)
          .build()
      textRecognizer.receiveFrame(frame)
    }
  }
  
  fun startCamera() {
    previewView!!.surfaceTextureListener = openGlCameraRenderer
  }
  
  fun release() {
    activity = null
    boxView = null
    previewView = null
    processImageButton = null
    onStringReady = null
  }
  
  private fun Bitmap.crop(rect: Rect): Bitmap {
    return Bitmap.createBitmap(this, rect.left, rect.top, rect.width(), rect.height())
  }
}