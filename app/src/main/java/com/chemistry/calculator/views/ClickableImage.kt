package com.chemistry.calculator.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.View
import com.chemistry.calculator.R
import com.chemistry.calculator.extensions.color

class ClickableImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private var image: Drawable?
  
  init {
    val arr = context.obtainStyledAttributes(attrs, R.styleable.ClickableImage, 0, 0)
    image = arr.getDrawable(R.styleable.ClickableImage_imageSrc)?.mutate()
    val defaultColor = context.color(R.color.light_button_background)
    val backgroundColor = arr.getColor(R.styleable.ClickableImage_backgroundColor, defaultColor)
    arr.recycle()
    
    val rippleColor = context.color(R.color.light_ripple)
    val roundShape = OvalShape()
    val backgroundDrawable = ShapeDrawable().apply {
      shape = roundShape
      paint.color = backgroundColor
    }
    val maskDrawable = ShapeDrawable().apply {
      shape = roundShape
      paint.color = rippleColor
    }
    background = RippleDrawable(ColorStateList.valueOf(rippleColor), backgroundDrawable, maskDrawable)
    isClickable = true
    isFocusable = true
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    image?.setBounds(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
  }
  
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val image = image ?: return
    image.draw(canvas)
  }
}