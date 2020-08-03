package com.chemistry.calculator.views

import android.animation.ValueAnimator
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
import com.chemistry.calculator.R
import com.chemistry.calculator.utils.AccelerateDecelerateInterpolator
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.i
import com.chemistry.calculator.utils.lerpColor
import com.chemistry.calculator.utils.tempRect
import com.chemistry.calculator.utils.tempRectF

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
  
  private var cornersSize = -1f
  private var cornersMargin = -1f
  private var cornersQuadLargeMargin = -1f
  private val cornersPath = Path()
  private val cornersPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    strokeJoin = Paint.Join.ROUND
    strokeCap = Paint.Cap.ROUND
    color = context.color(R.color.light_primary)
  }
  
  private var isMoving = false
  private var lastX = -1f
  private var lastY = -1f
  
  private var leftDragLimit = -1f
  private var topDragLimit = -1f
  private var rightDragLimit = -1f
  private var bottomDragMaxLimit = -1f
  
  private var distanceToLeft = -1f
  private var distanceToTop = -1f
  
  private val appearanceAnimator = ValueAnimator().apply {
    duration = APPEARANCE_DURATION
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      val fraction = it.animatedValue as Float
      fillScreenPaint.color = lerpColor(Color.BLACK, COLOR_SHADOW, fraction)
      invalidate()
    }
  }
  
  val frameBox: Rect
    get() {
      tempRectF.set(boxRect)
      tempRectF.inset(cornersRadius, cornersRadius)
      tempRectF.roundOut(tempRect)
      return tempRect
    }
  
  fun animateAppearance() {
    appearanceAnimator.setFloatValues(0f, 1f)
    appearanceAnimator.start()
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
    cornersSize = boxRect.height() / 6f
    cornersMargin = boxRect.height() / 15f
    cornersQuadLargeMargin = boxRect.height() / 40f
    updateCorners()
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
          updateCorners()
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
    canvas.drawPath(cornersPath, cornersPaint)
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
  
  private fun updateCorners() {
    val left = boxRect.left
    val top = boxRect.top
    val right = boxRect.right
    val bottom = boxRect.bottom
    cornersPath.reset()
    with(cornersPath) {
      // Top left corner
      moveTo(left + cornersMargin, top + cornersMargin + cornersSize)
      cubicTo(
        left + cornersMargin, top + cornersMargin - cornersQuadLargeMargin,
        left + cornersMargin - cornersQuadLargeMargin, top + cornersMargin,
        left + cornersMargin + cornersSize, top + cornersMargin
      )
      close()
      
      // Bottom left corner
      moveTo(left + cornersMargin, bottom - cornersMargin - cornersSize)
      cubicTo(
        left + cornersMargin, bottom - cornersMargin + cornersQuadLargeMargin,
        left + cornersMargin - cornersQuadLargeMargin, bottom - cornersMargin,
        left + cornersMargin + cornersSize, bottom - cornersMargin
      )
      close()
  
      // Top right corner
      moveTo(right - cornersMargin - cornersSize, top + cornersMargin)
      cubicTo(
        right - cornersMargin + cornersQuadLargeMargin, top + cornersMargin,
        right - cornersMargin, top + cornersMargin - cornersQuadLargeMargin,
        right - cornersMargin, top + cornersMargin + cornersSize
      )
      close()
    }
  }
  
  companion object {
    private const val COLOR_SHADOW = 0x77000000
    private const val APPEARANCE_DURATION = 600L
  }
}
