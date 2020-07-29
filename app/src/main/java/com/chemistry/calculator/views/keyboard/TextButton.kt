package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.TextPaint
import android.view.View
import com.chemistry.calculator.R
import com.chemistry.calculator.core.ELEMENT_BUTTON_CORNERS_COEFFICIENT
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.createRoundedRipple
import com.chemistry.calculator.utils.tempRect

@SuppressLint("ViewConstructor") // Created only through code
class TextButton(
  context: Context,
  private val text: String,
  private val textSize: Float,
  private val textColor: Int,
  private val backgroundColor: Int,
  private val drawWithDescent: Boolean = false,
  private val rippleColor: Int = context.color(R.color.light_ripple),
  var onClicked: (String) -> Unit = {}
) : View(context) {
  
  private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
    textAlign = Paint.Align.CENTER
    textSize = this@TextButton.textSize
    color = textColor
  }
  private var cornersRadius = -1f
  private var textYTranslate = -1f
  
  init {
    setOnClickListener { onClicked(text) }
    isClickable = true
    isFocusable = true
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    cornersRadius = minOf(w, h) / ELEMENT_BUTTON_CORNERS_COEFFICIENT
    textPaint.getTextBounds(text, 0, text.length, tempRect)
    textYTranslate = height / 2f + tempRect.height() / 2f
    background = createRoundedRipple(cornersRadius, backgroundColor, rippleColor)
  }
  
  override fun onDraw(canvas: Canvas) {
    if (drawWithDescent) {
      canvas.drawText(text, width / 2f, textYTranslate - textPaint.descent(), textPaint)
    } else {
      canvas.drawText(text, width / 2f, textYTranslate, textPaint)
    }
  }
}