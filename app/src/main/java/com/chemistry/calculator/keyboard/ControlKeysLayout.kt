package com.chemistry.calculator.keyboard

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.extensions.dimen
import com.chemistry.calculator.extensions.forEachChild
import com.chemistry.calculator.extensions.i

class ControlKeysLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private val textSize = context.dimen(R.dimen.text_h1)
  private val elementsPadding = context.dimen(R.dimen.keyboard_elements_padding)
  private var elementWidth = -1f
  
  private val moreButton = TextButton(
    context,
    text = "...",
    textSize = this@ControlKeysLayout.textSize,
    textColor = Color.BLACK,
    backgroundColor = Color.GRAY
  )
  
  private val openBracketButton = TextButton(
    context,
    text = "A)",
    textSize = this@ControlKeysLayout.textSize,
    textColor = Color.BLACK,
    backgroundColor = Color.GRAY
  )
  
  private val closeBracketButton = TextButton(
    context,
    text = ")",
    textSize = this@ControlKeysLayout.textSize,
    textColor = Color.BLACK,
    backgroundColor = Color.GRAY
  )
  private val backspaceButton = TextButton(
    context,
    text = "<-",
    textSize = this@ControlKeysLayout.textSize,
    textColor = Color.BLACK,
    backgroundColor = Color.GRAY
  )
  
  init {
    addView(moreButton)
    addView(openBracketButton)
    addView(closeBracketButton)
    addView(backspaceButton)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val elementWidth = (width - elementsPadding * 5) / 4
    setMeasuredDimension(
      resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
      resolveSize((elementWidth / 1.8f).toInt(), heightMeasureSpec)
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    elementWidth = (w - (elementsPadding * 5)) / 4f
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var left = elementsPadding
    forEachChild { child ->
      child.layout(
        left.i,
        0,
        (left + elementWidth).i,
        height
      )
      left += elementWidth + elementsPadding
    }
  }
}