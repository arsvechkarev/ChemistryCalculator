package com.chemistry.calculator.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
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
import com.chemistry.calculator.extensions.tempRect
import com.chemistry.calculator.extensions.tempRectF

class BoxView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private val fillScreenPath = Path()
  private val fillScreenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = COLOR_SHADOW }
  private val boxPath = Path()
  private val boxRect = RectF()
  private var cornersRadius = -1f
  
  private val imageDragBoxRect = RectF()
  private var imageDragBoxSize = -1f
  private val imageDragBox = DragBoxDrawable(context)
  
  private var isMoving = false
  private var lastX = -1f
  private var lastY = -1f
  
  private var leftDragLimit = -1f
  private var topDragLimit = -1f
  private var rightDragLimit = -1f
  private var bottomDragMaxLimit = -1f
  
  private var distanceToLeft = -1f
  private var distanceToTop = -1f
  
  val frameBox: Rect
    get() {
      tempRectF.set(boxRect)
      tempRectF.inset(cornersRadius, cornersRadius)
      tempRectF.roundOut(tempRect)
      return tempRect
    }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val left = w / 6f
    val top = w / 4f
    val right = w - left
    val bottom = top + w / 6f
    cornersRadius = left / 15f
    imageDragBoxSize = w / 25f
    initBoxFrame(left, top, right, bottom)
    fillScreenPath.addRect(0f, 0f, w.toFloat(), h.toFloat(), Path.Direction.CW)
    imageDragBoxRect.set(
      boxRect.right - imageDragBoxSize,
      boxRect.bottom - imageDragBoxSize,
      boxRect.right + imageDragBoxSize,
      boxRect.bottom + imageDragBoxSize
    )
    imageDragBox.setBounds(
      imageDragBoxRect.left.i,
      imageDragBoxRect.top.i,
      imageDragBoxRect.right.i,
      imageDragBoxRect.bottom.i
    )
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y
    when (event.action) {
      ACTION_DOWN -> {
        if (imageDragBoxRect.contains(x, y)) {
          lastX = x
          lastY = y
          distanceToLeft = x - imageDragBoxRect.left
          distanceToTop = y - imageDragBoxRect.top
          isMoving = true
          return true
        }
      }
      ACTION_MOVE -> {
        if (isMoving) {
          val distanceFromFingerToCenterX = imageDragBoxRect.width() / 2f - distanceToLeft
          val distanceFromFingerToCenterY = imageDragBoxRect.height() / 2f - distanceToTop
          val newX = x.coerceIn(
            leftDragLimit - distanceFromFingerToCenterX,
            rightDragLimit - distanceFromFingerToCenterX
          )
          val newY = y.coerceIn(
            topDragLimit - distanceFromFingerToCenterY,
            bottomDragMaxLimit - distanceFromFingerToCenterY
          )
          imageDragBoxRect.offsetTo(newX - distanceToLeft, newY - distanceToTop)
          imageDragBox.setBounds(
            imageDragBoxRect.left.i,
            imageDragBoxRect.top.i,
            imageDragBoxRect.right.i,
            imageDragBoxRect.bottom.i
          )
          val dx = newX - lastX
          val dy = newY - lastY
          boxRect.left -= dx
          boxRect.top -= dy
          boxRect.right += dx
          boxRect.bottom += dy
          boxPath.reset()
          boxPath.addRoundRect(boxRect, cornersRadius, cornersRadius, Path.Direction.CW)
          lastX = newX
          lastY = newY
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
  }
  
  private fun initBoxFrame(left: Float, top: Float, right: Float, bottom: Float) {
    boxRect.set(left, top, right, bottom)
    val maxOffset = boxRect.height() / 2
    leftDragLimit = boxRect.right - boxRect.height()
    topDragLimit = boxRect.bottom - boxRect.height() / 4
    rightDragLimit = boxRect.right + maxOffset
    bottomDragMaxLimit = boxRect.bottom + maxOffset
    boxPath.addRoundRect(
      left, top, right, bottom, cornersRadius, cornersRadius, Path.Direction.CW
    )
  }
  
  companion object {
    const val COLOR_SHADOW = 0x77000000
  }
}
