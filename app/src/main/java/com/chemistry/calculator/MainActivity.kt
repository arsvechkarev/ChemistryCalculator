package com.chemistry.calculator

//import kotlinx.android.synthetic.main.activity_main.previewView
import android.os.Bundle
import android.view.TextureView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.chemistry.calculator.core.Application
import com.chemistry.calculator.features.camera.CameraScreen
import com.chemistry.calculator.features.camera.opengl.CameraRenderer
import com.chemistry.calculator.features.solving.EquationSolvingScreen
import kotlinx.android.synthetic.main.activity_main.container

class MainActivity : AppCompatActivity() {
  
  private lateinit var permissionHelper: PermissionHelper
  private lateinit var cameraScreen: CameraScreen
  private lateinit var equationSolvingScreen: EquationSolvingScreen
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Application.initResources(resources)
    
    val frameLayout = FrameLayout(this)
    setContentView(frameLayout)
  
    val cameraRenderer = CameraRenderer(this)
    val textureView = TextureView(this)
    frameLayout.addView(textureView)
    textureView.surfaceTextureListener = cameraRenderer
    
    textureView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
      cameraRenderer.onSurfaceTextureSizeChanged(null, v.width, v.height)
    }
    //    initializeScreen()
  }
  
  private fun initializeScreen() {
//    cameraScreen = CameraScreen(this, boxView, previewView, processImageButton,
//      boxView::frameBox, ::processEquation)
    //    equationSolvingScreen = EquationSolvingScreen(equationEditText, keyboard)
    //    permissionHelper = PermissionHelper(this)
    //    if (permissionHelper.isCameraGranted) {
    //      cameraScreen.startCamera()
    //    } else {
    //      permissionHelper.requestCameraPermission()
    //    }
    //    openKeyboardButton.setOnClickListener { bottomSheet.show() }
  }
  
  private fun processEquation(symbol: String) {
//    AndroidThreader.onMainThread {
//      bottomSheet.show()
//      equationSolvingScreen.processEquation(symbol)
//    }
  }
  
  override fun onStop() {
    super.onStop()
//    cameraScreen.release()
  }
}