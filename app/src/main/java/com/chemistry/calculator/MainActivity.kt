package com.chemistry.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.chemistry.calculator.core.Application
import com.chemistry.calculator.features.solving.EquationSolvingScreen
import com.chemistry.calculator.features.camera.ImageProcessor
import kotlinx.android.synthetic.main.activity_main.bottomSheet
import kotlinx.android.synthetic.main.activity_main.boxView
import kotlinx.android.synthetic.main.activity_main.editText
import kotlinx.android.synthetic.main.activity_main.keyboard
import kotlinx.android.synthetic.main.activity_main.previewView
import kotlinx.android.synthetic.main.activity_main.processImageButton

class MainActivity : AppCompatActivity() {
  
  private lateinit var permissionHelper: PermissionHelper
  private lateinit var imageProcessor: ImageProcessor
  private lateinit var equationSolvingScreen: EquationSolvingScreen
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Application.initResources(resources)
    setContentView(R.layout.activity_main)
    permissionHelper = PermissionHelper(this)
    imageProcessor = ImageProcessor(this, boxView,
      previewView, boxView::frameBox, ::processEquation)
    equationSolvingScreen = EquationSolvingScreen(
      editText, keyboard)
    previewView.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
    if (permissionHelper.isCameraGranted) {
      imageProcessor.startCamera()
    } else {
      permissionHelper.requestCameraPermission()
    }
    processImageButton.setOnClickListener {
      bottomSheet.show()
//      imageProcessor.processImage()
    }
  }
  
  private fun processEquation(symbol: String) {
    equationSolvingScreen.processSymbol(symbol)
  }
  
  override fun onStop() {
    super.onStop()
    imageProcessor.release()
  }
}