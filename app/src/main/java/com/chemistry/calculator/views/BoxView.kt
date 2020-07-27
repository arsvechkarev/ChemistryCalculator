package com.chemistry.calculator.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import com.chemistry.calculator.extensions.i

class BoxView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private val fillScreenPath = Path()
  private val fillScreenPaint = Paint().apply { color = COLOR_SHADOW }
  private val boxPath = Path()
  private val boxRect = RectF()
  private var cornersRadius = -1f
  
  private val imageDragBoxRect = Rect()
  private var imageDragBoxSize = -1
  private val imageDragBox = DragBoxDrawable(context)
  
  private var isMoving = false
  private var lastX = -1f
  private var lastY = -1f
  
  private var leftDragMaxLimit = -1f
  private var topDragMaxLimit = -1f
  private var rightDragMaxLimit = -1f
  private var bottomDragMaxLimit = -1f
  
  private var leftDragMinLimit = -1f
  private var topDragMinLimit = -1f
  private var rightDragMinLimit = -1f
  private var bottomDragMinLimit = -1f
  
  val frameBox = Rect()
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val left = w / 6f
    val top = w / 4f
    val right = w - left
    val bottom = top + w / 6f
    cornersRadius = left / 15f
    imageDragBoxSize = w / 25
    initBoxFrame(left, top, right, bottom)
    fillScreenPath.addRect(0f, 0f, w.toFloat(), h.toFloat(), Path.Direction.CW)
    imageDragBoxRect.set(
      frameBox.right - imageDragBoxSize,
      frameBox.bottom - imageDragBoxSize,
      frameBox.right + imageDragBoxSize,
      frameBox.bottom + imageDragBoxSize
    )
    imageDragBox.bounds = imageDragBoxRect
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y
    when (event.action) {
      ACTION_DOWN -> {
        if (imageDragBoxRect.contains(x.i, y.i)) {
          lastX = x
          lastY = y
          isMoving = true
          return true
        }
      }
      ACTION_MOVE -> {
        val xInTheZone = (x > leftDragMaxLimit && x < leftDragMinLimit
            || x > rightDragMinLimit && x < rightDragMaxLimit)
        val yInTheZone = (y > topDragMaxLimit && y < topDragMinLimit
            || y > bottomDragMinLimit && y < bottomDragMaxLimit)
        if (isMoving && xInTheZone && yInTheZone) {
          val dx = x - lastX
          val dy = y - lastY
          frameBox.offset(dx.i, dy.i)
          boxRect.left -= dx
          boxRect.top -= dy
          boxRect.right += dx
          boxRect.bottom += dy
          boxRect.left = boxRect.left.coerceIn(leftDragMaxLimit, leftDragMinLimit)
          boxRect.top = boxRect.top.coerceIn(topDragMaxLimit, topDragMinLimit)
          boxRect.right = boxRect.right.coerceIn(rightDragMinLimit, rightDragMaxLimit)
          boxRect.bottom = boxRect.bottom.coerceIn(bottomDragMinLimit, bottomDragMaxLimit)
          boxPath.reset()
          boxPath.addRoundRect(boxRect, cornersRadius, cornersRadius, Path.Direction.CW)
          imageDragBoxRect.offset(dx.i, dy.i)
          imageDragBox.bounds = imageDragBoxRect
          lastX = x
          lastY = y
          invalidate()
          return true
        }
      }
    }
    return false
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.save()
    @Suppress("DEPRECATION")
    canvas.clipPath(boxPath, Region.Op.DIFFERENCE)
    canvas.drawPath(fillScreenPath, fillScreenPaint)
    canvas.restore()
    imageDragBox.draw(canvas)
    canvas.drawRect(leftDragMaxLimit, topDragMaxLimit, rightDragMaxLimit, bottomDragMaxLimit, Paint().apply {
      style = Paint.Style.STROKE
      strokeWidth = 2f
      color = Color.RED
    })
    canvas.drawRect(leftDragMinLimit, topDragMinLimit, rightDragMinLimit, bottomDragMinLimit, Paint().apply {
      style = Paint.Style.STROKE
      strokeWidth = 2f
      color = Color.RED
    })
  }
  
  private fun initBoxFrame(left: Float, top: Float, right: Float, bottom: Float) {
    boxRect.set(left, top, right, bottom)
    val maxOffset = boxRect.height() / 2
    leftDragMaxLimit = boxRect.left - maxOffset
    topDragMaxLimit = boxRect.top - maxOffset
    rightDragMaxLimit = boxRect.right + maxOffset
    bottomDragMaxLimit = boxRect.bottom + maxOffset
    val minVerticalOffset = boxRect.height() / 4
    val minHorizontalOffset = boxRect.height()
    leftDragMinLimit = boxRect.left + minHorizontalOffset
    topDragMinLimit = boxRect.top + minVerticalOffset
    rightDragMinLimit = boxRect.right - minHorizontalOffset
    bottomDragMinLimit = boxRect.bottom - minVerticalOffset
    boxPath.addRoundRect(
      left, top, right, bottom, cornersRadius, cornersRadius, Path.Direction.CW
    )
    frameBox.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    frameBox.inset(cornersRadius.i, cornersRadius.i)
  }
  
  companion object {
    const val COLOR_SHADOW = 0x77000000
  }
}