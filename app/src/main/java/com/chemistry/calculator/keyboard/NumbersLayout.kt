package com.chemistry.calculator.keyboard

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.extensions.dimen
import com.chemistry.calculator.extensions.f
import com.chemistry.calculator.extensions.forEachChild
import com.chemistry.calculator.extensions.i

class NumbersLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private var numberHeight = -1f
  private var numberWidth = -1f
  
  private val textSize = context.dimen(R.dimen.text_h2)
  private val smallPadding = context.dimen(R.dimen.keyboard_small_padding)
  
  init {
    repeat(10) { i ->
      addView(TextButton(
        context = context,
        text = ((i + 1) % 10).toString(),
        textSize = textSize,
        textColor = Color.BLACK,
        backgroundColor = Color.WHITE
      ))
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val height = textSize * 2 + smallPadding * 2
    setMeasuredDimension(
      resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
      resolveSize(height.toInt(), heightMeasureSpec)
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    numberWidth = (w - (smallPadding * 11)) / 10f
    numberHeight = h.f
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var left = smallPadding
    forEachChild { child ->
      child.layout(
        left.i,
        smallPadding.i,
        (left + numberWidth).i,
        height - smallPadding.i
      )
      left += numberWidth + smallPadding
    }
  }
}