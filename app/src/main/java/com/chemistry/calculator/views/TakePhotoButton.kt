package com.chemistry.calculator.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TakePhotoButton @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)
  }
}