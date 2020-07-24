package com.chemistry.calculator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Region
import android.util.AttributeSet
import android.view.View

class BoxView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private val fillPath = Path()
  private val boxPath = Path()
  private val paint = Paint().apply {
    color = COLOR_SHADOW.toInt()
  }
  
  val frameBox = Rect()
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val left = w / 6f
    val top = w / 4f
    val right = w - left
    val bottom = top + w / 6f
    boxPath.addRoundRect(
      left, top, right, bottom, left / 10f, left / 10f, Path.Direction.CW
    )
    frameBox.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    fillPath.addRect(
      0f,
      0f,
      w.toFloat(),
      h.toFloat(),
      Path.Direction.CW
    )
    println("eeex w: $w")
    println("eeex h: $h")
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.clipPath(boxPath, Region.Op.DIFFERENCE)
    canvas.drawPath(fillPath, paint)
    canvas.restore()
  }
  
  companion object {
    const val COLOR_SHADOW = 0x99000000
  }
}