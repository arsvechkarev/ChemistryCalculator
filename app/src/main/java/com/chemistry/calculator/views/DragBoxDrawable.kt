package com.chemistry.calculator.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.chemistry.calculator.R
import com.chemistry.calculator.utils.color

class DragBoxDrawable(context: Context) : Drawable() {
  
  private val rect = Rect()
  private val icon = context.getDrawable(R.drawable.ic_drag_box)!!
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = context.color(R.color.light_main)
  }
  
  override fun onBoundsChange(bounds: Rect) {
    rect.set(bounds)
    bounds.inset(bounds.width() / 6, bounds.height() / 6)
    icon.bounds = bounds
  }
  
  override fun draw(canvas: Canvas) {
    canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(), rect.width() / 2f, circlePaint)
    icon.draw(canvas)
  }
  
  override fun setAlpha(alpha: Int) {
    circlePaint.alpha = alpha
    icon.alpha = alpha
  }
  
  override fun getOpacity() = PixelFormat.OPAQUE
  
  override fun setColorFilter(colorFilter: ColorFilter?) {}
}