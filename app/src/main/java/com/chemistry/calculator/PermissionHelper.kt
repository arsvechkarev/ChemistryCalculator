package com.chemistry.calculator

import androidx.core.app.ActivityCompat

class PermissionHelper(private val mainActivity: MainActivity) {
  
  val isCameraGranted: Boolean
    get() = true
  
  fun requestCameraPermission() {
    ActivityCompat.requestPermissions(mainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
  }
  
  companion object {
    private val REQUEST_CODE_PERMISSIONS = 99
    private val REQUIRED_PERMISSIONS = arrayOf(
      "android.permission.CAMERA",
      "android.permission.WRITE_EXTERNAL_STORAGE"
    )
  }
}