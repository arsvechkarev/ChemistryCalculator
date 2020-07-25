package com.chemistry.calculator.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.view.View
import com.chemistry.calculator.extensions.f
import com.chemistry.calculator.extensions.tempRect

@SuppressLint("ViewConstructor") // Created only through code
class TextButton(
  context: Context,
  private val text: String,
  private val textSize: Float,
  private val textColor: Int,
  private val backgroundColor: Int,
  var onClicked: (String) -> Unit = {}
) : View(context) {
  
  private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = backgroundColor
  }
  private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
    textAlign = Paint.Align.CENTER
    textSize = this@TextButton.textSize
    color = textColor
  }
  private var cornersRadius = -1f
  private var textYTranslate = -1f
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    cornersRadius = minOf(w, h) / 20f
    textPaint.getTextBounds(text, 0, text.length, tempRect)
    textYTranslate = height / 2f + tempRect.height() / 2f
    tempRect.setEmpty()
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawRoundRect(0f, 0f, width.f, height.f, cornersRadius, cornersRadius, backgroundPaint)
    canvas.drawText(text, width / 2f, textYTranslate, textPaint)
  }
  
  override fun performClick(): Boolean {
    onClicked(text)
    return super.performClick()
  }
}