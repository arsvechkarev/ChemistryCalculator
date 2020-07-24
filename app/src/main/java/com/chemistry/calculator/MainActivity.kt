package com.chemistry.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import kotlinx.android.synthetic.main.activity_main.bottomSheet
import kotlinx.android.synthetic.main.activity_main.boxView
import kotlinx.android.synthetic.main.activity_main.previewView
import kotlinx.android.synthetic.main.activity_main.processImageButton
import timber.log.Timber

class MainActivity : AppCompatActivity() {
  
  private lateinit var permissionHelper: PermissionHelper
  private lateinit var imageProcessor: ImageProcessor
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    permissionHelper = PermissionHelper(this)
    imageProcessor = ImageProcessor(this,boxView, previewView, boxView::frameBox, ::processEquation)
    previewView.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
    if (permissionHelper.isCameraGranted) {
      imageProcessor.startCamera()
    } else {
      permissionHelper.requestCameraPermission()
    }
    processImageButton.setOnClickListener {
      bottomSheet.show()
      imageProcessor.processImage()
    }
  }
  
  private fun processEquation(s: String) {
    Timber.d(s)
  }
}