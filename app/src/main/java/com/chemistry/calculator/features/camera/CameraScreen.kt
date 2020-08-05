package com.chemistry.calculator.features.camera

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chemistry.calculator.core.async.AndroidThreader
import com.chemistry.calculator.views.BoxView
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.picturesDirectory: File
  get() {
    val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(picturesDirectory, "${File.separator}Chem${File.separator}Pics")
  }

class CameraScreen(
  private var activity: AppCompatActivity?,
  private var boxView: BoxView?,
  private var previewView: TextureView?,
  private var processImageButton: View?,
  private var onStringReady: ((String) -> Unit)?
) {
  
  private val openGlCameraRenderer = CameraRenderer(onPreviewStarted = {
    AndroidThreader.onMainThread { boxView?.animateAppearance() }
  })
  
  init {
    boxView!!.setOnClickListener {
      openGlCameraRenderer.onClick()
    }
    processImageButton!!.setOnClickListener {
      val bitmap = previewView!!.bitmap
      val directory = it.context.picturesDirectory
      directory.mkdirs()
      val dateFormat = SimpleDateFormat("yyyy_MM_dd-HH_mm_ss", Locale.getDefault())
      val timestamp = dateFormat.format(Date())
      val projectFile = File(directory, "Image_$timestamp.png")
      Timber.d("Saving image, file = ${projectFile.path}")
      FileOutputStream(projectFile).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
      }
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
}