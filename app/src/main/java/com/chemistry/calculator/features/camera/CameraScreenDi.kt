package com.chemistry.calculator.features.camera

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.text.TextRecognizer

object CameraScreenDi {
  
  fun provideTextRecognizer(activity: AppCompatActivity): TextRecognizer {
    return TextRecognizer.Builder(activity).build()
  }
}