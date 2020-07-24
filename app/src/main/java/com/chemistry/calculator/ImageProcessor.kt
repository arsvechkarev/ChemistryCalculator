package com.chemistry.calculator

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Environment
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val AppCompatActivity.allProjectsDirectory: File
  get() {
    val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(picturesDirectory, "$${File.separator}PhotoScans")
  }

class ImageProcessor(
  private var activity: AppCompatActivity?,
  private var boxView: BoxView?,
  private var previewView: PreviewView?,
  private var cropRectProvider: (() -> Rect)?,
  private var onStringReady: ((String) -> Unit)?,
  private val executor: ExecutorService = Executors.newSingleThreadExecutor()
) {
  
  private val textRecognizer = TextRecognizer.Builder(activity).build()
  private lateinit var imageCapture: ImageCapture
  
  init {
    textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
      override fun release() {}
      override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
        val items = detections.detectedItems
        println("www:============== ${items.size()}")
        println("www:${cropRectProvider!!.invoke().toShortString()}")
        for (i in 0 until items.size()) {
          val textBlock = items.valueAt(i)
          println("www:$i = ${textBlock.boundingBox.toShortString()}")
          println("www:$i = ${textBlock.value}")
        }
        if (items.size() != 0) {
          onStringReady?.invoke(items[0].value)
        }
      }
    })
  }
  
  fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(activity!!)
    cameraProviderFuture.addListener(Runnable {
      val cameraProvider = cameraProviderFuture.get()
      val preview = Preview.Builder()
          .setTargetRotation(Surface.ROTATION_0)
          .build()
      val cameraSelector = CameraSelector.Builder()
          .requireLensFacing(CameraSelector.LENS_FACING_BACK)
          .build()
      imageCapture = ImageCapture.Builder()
          .setIoExecutor(executor)
          .setTargetRotation(Surface.ROTATION_0)
          .build()
      preview.setSurfaceProvider(previewView!!.createSurfaceProvider())
      cameraProvider.bindToLifecycle((activity!!), cameraSelector, preview, imageCapture)
    }, ContextCompat.getMainExecutor(activity!!))
  }
  
  fun processImage() {
    imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
      
      @SuppressLint("UnsafeExperimentalUsageError")
      override fun onCaptureSuccess(image: ImageProxy) {
        val rect = cropRectProvider!!.invoke()
        val bitmap = image.toBitmap().rotate().crop(rect, boxView!!)
        val frame = Frame.Builder()
            .setBitmap(bitmap)
            .build()
        textRecognizer.receiveFrame(frame)
        image.close()
      }
      
      override fun onError(error: ImageCaptureException) {
        error.printStackTrace()
      }
    })
  }
  
  private fun saveBitmap(bitmap: Bitmap) {
    val directory = activity!!.allProjectsDirectory
    directory.mkdirs()
    val dateFormat = SimpleDateFormat("yyyy_MM_dd-HH_mm_ss", Locale.getDefault())
    val timestamp = dateFormat.format(Date())
    val projectFile = File(directory, "Project_$timestamp.jpg")
    FileOutputStream(projectFile).use {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
  }
  
  fun release() {
    activity = null
    onStringReady = null
    executor.shutdownNow()
    textRecognizer.release()
  }
}
