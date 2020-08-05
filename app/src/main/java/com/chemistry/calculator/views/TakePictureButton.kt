package com.chemistry.calculator.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.chemistry.calculator.R
import com.chemistry.calculator.utils.AccelerateDecelerateInterpolator
import com.chemistry.calculator.utils.cancelIfRunning
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.execute
import com.chemistry.calculator.utils.happenedIn

class TakePictureButton @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private var circleOffset = -1f
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = context.color(R.color.light_button_background)
  }
  
  private lateinit var shadowGradient: RadialGradient
  private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  
  private var scaleFactor = 1f
  private val scaleAnimator = ValueAnimator().apply {
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      scaleFactor = it.animatedValue as Float
      invalidate()
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    circleOffset = w / 15f
    shadowGradient = RadialGradient(
      w / 2f,
      h / 2f,
      minOf(w, h) / 2f,
      intArrayOf(context.color(R.color.light_primary), Color.TRANSPARENT),
      floatArrayOf(0.9f, 1f),
      Shader.TileMode.CLAMP
    )
    shadowPaint.shader = shadowGradient
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        scaleAnimator.cancelIfRunning()
        scaleAnimator.duration = 50
        scaleAnimator.setFloatValues(scaleFactor, 0.9f)
        scaleAnimator.start()
        return true
      }
      ACTION_MOVE -> {
        if (!(event happenedIn this)) {
          scaleAnimator.cancelIfRunning()
          scaleAnimator.duration = 160
          scaleAnimator.setFloatValues(scaleFactor, 1.07f, 1f)
          scaleAnimator.start()
          return false
        }
      }
      ACTION_UP -> {
        scaleAnimator.cancelIfRunning()
        scaleAnimator.duration = 160
        scaleAnimator.setFloatValues(scaleFactor, 1.07f, 1f)
        scaleAnimator.start()
        if (event happenedIn this) {
          performClick()
        }
        return true
      }
    }
    return true
  }
  
  override fun onDraw(canvas: Canvas) {
    execute(canvas) {
      scale(scaleFactor, scaleFactor, width / 2f, height / 2f)
      drawCircle(width / 2f, height / 2f, width / 2f, shadowPaint)
      drawCircle(width / 2f, height / 2f, width / 2f - circleOffset, circlePaint)
    }
  }
}