package com.chemistry.calculator.views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.core.BACKSPACE_SYMBOL
import com.chemistry.calculator.core.CLOSE_BRACKET_SYMBOL
import com.chemistry.calculator.core.MORE_SYMBOL
import com.chemistry.calculator.core.OPEN_BRACKET_SYMBOL
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.dimen
import com.chemistry.calculator.utils.forEachChild
import com.chemistry.calculator.utils.i

class ControlsLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private val textSize = context.dimen(R.dimen.text_h1)
  private val elementsPadding = context.dimen(R.dimen.keyboard_elements_padding)
  private var elementWidth = -1f
  
  var onItemClicked: (String) -> Unit = {}
  
  private val moreButton = TextButton(
    context,
    text = MORE_SYMBOL,
    textSize = context.dimen(R.dimen.text_h0),
    textColor = context.color(R.color.light_text),
    backgroundColor = context.color(R.color.light_control_button),
    onClicked = { onItemClicked(it) }
  )
  
  private val openBracketButton = TextButton(
    context,
    text = OPEN_BRACKET_SYMBOL,
    textSize = this@ControlsLayout.textSize,
    textColor = context.color(R.color.light_text),
    backgroundColor = context.color(R.color.light_control_button),
    drawWithDescent = true,
    onClicked = { onItemClicked(it) }
  )
  
  private val closeBracketButton = TextButton(
    context,
    text = CLOSE_BRACKET_SYMBOL,
    textSize = this@ControlsLayout.textSize,
    textColor = context.color(R.color.light_text),
    backgroundColor = context.color(R.color.light_control_button),
    drawWithDescent = true,
    onClicked = { onItemClicked(it) }
  )
  
  private val backspaceButton = ClickAndHoldIconButton(
    context,
    id = BACKSPACE_SYMBOL,
    iconRes = R.drawable.ic_backspace,
    iconColor = context.color(R.color.light_text),
    backgroundColor = context.color(R.color.light_control_button),
    onClicked = { onItemClicked(it) },
    onHold = { onItemClicked(it) }
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
      child.layout(left.i, 0, (left + elementWidth).i, height)
      left += elementWidth + elementsPadding
    }
  }
}