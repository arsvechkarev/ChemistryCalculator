package com.chemistry.calculator.features.camera

import android.graphics.Rect
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.chemistry.calculator.views.BoxView

class CameraScreen(
  private var activity: AppCompatActivity?,
  private var boxView: BoxView?,
  private var previewView: PreviewView?,
  private var processImageButton: View?,
  private var cropRectProvider: (() -> Rect)?,
  private var onStringReady: ((String) -> Unit)?
) {
  
  private val imageProcessor = ImageProcessor(
    activity,
    boxView,
    previewView,
    cropRectProvider,
    onStringReady
  )
  
  init {
    previewView!!.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
    processImageButton!!.setOnClickListener { imageProcessor.processImage() }
  }
  
  fun startCamera() {
    imageProcessor.startCamera()
  }
  
  fun release() {
    activity = null
    boxView = null
    previewView = null
    processImageButton = null
    cropRectProvider = null
    onStringReady = null
    imageProcessor.release()
  }
}