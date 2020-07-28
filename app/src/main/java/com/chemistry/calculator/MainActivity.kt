package com.chemistry.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chemistry.calculator.core.Application
import com.chemistry.calculator.core.async.AndroidThreader
import com.chemistry.calculator.features.camera.CameraScreen
import com.chemistry.calculator.features.solving.EquationSolvingScreen
import kotlinx.android.synthetic.main.activity_main.bottomSheet
import kotlinx.android.synthetic.main.activity_main.boxView
import kotlinx.android.synthetic.main.activity_main.equationEditText
import kotlinx.android.synthetic.main.activity_main.keyboard
import kotlinx.android.synthetic.main.activity_main.openKeyboardButton
import kotlinx.android.synthetic.main.activity_main.previewView
import kotlinx.android.synthetic.main.activity_main.processImageButton

class MainActivity : AppCompatActivity() {
  
  private lateinit var permissionHelper: PermissionHelper
  private lateinit var cameraScreen: CameraScreen
  private lateinit var equationSolvingScreen: EquationSolvingScreen
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Application.initResources(resources)
    setContentView(R.layout.activity_main)
    initializeScreen()
  }
  
  private fun initializeScreen() {
    cameraScreen = CameraScreen(this, boxView, previewView, processImageButton,
      boxView::frameBox, ::processEquation)
    equationSolvingScreen = EquationSolvingScreen(equationEditText, keyboard)
    permissionHelper = PermissionHelper(this)
    if (permissionHelper.isCameraGranted) {
      cameraScreen.startCamera()
    } else {
      permissionHelper.requestCameraPermission()
    }
    openKeyboardButton.setOnClickListener { bottomSheet.show() }
  }
  
  private fun processEquation(symbol: String) {
    AndroidThreader.onMainThread {
      bottomSheet.show()
      equationSolvingScreen.processEquation(symbol)
    }
  }
  
  override fun onStop() {
    super.onStop()
    cameraScreen.release()
  }
}