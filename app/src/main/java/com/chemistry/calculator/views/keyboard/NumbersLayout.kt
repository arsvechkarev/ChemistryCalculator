package com.chemistry.calculator.views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.dimen
import com.chemistry.calculator.utils.forEachChild
import com.chemistry.calculator.utils.i

class NumbersLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private val textSize = context.dimen(R.dimen.text_h2)
  private val elementsPadding = context.dimen(R.dimen.keyboard_elements_padding)
  private var numberWidth = -1f
  
  var onItemClicked: (String) -> Unit = {}
  
  init {
    repeat(10) { i ->
      addView(TextButton(
        context = context,
        text = ((i + 1) % 10).toString(),
        textSize = textSize,
        textColor = context.color(R.color.light_text),
        backgroundColor = context.color(R.color.light_control_button),
        onClicked = { onItemClicked(it) }
      ))
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val height = textSize * 2 + elementsPadding * 2
    setMeasuredDimension(
      resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
      resolveSize(height.toInt(), heightMeasureSpec)
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    numberWidth = (w - (elementsPadding * 11)) / 10f
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var left = elementsPadding
    forEachChild { child ->
      child.layout(
        left.i,
        elementsPadding.i,
        (left + numberWidth).i,
        height - elementsPadding.i
      )
      left += numberWidth + elementsPadding
    }
  }
}