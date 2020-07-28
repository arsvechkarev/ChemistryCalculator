package com.chemistry.calculator.views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.core.ELEMENTS
import com.chemistry.calculator.core.EQUALS_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.dimen
import com.chemistry.calculator.utils.f
import com.chemistry.calculator.utils.i

class ElementsLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private val textSize = context.dimen(R.dimen.text_h1)
  private val elementsPadding = context.dimen(R.dimen.keyboard_elements_padding).toInt()
  private var elementSize = -1f
  
  var onItemClicked: (String) -> Unit = {}
  
  private val plusButton = TextButton(
    context,
    text = PLUS_SYMBOL,
    textSize = this@ElementsLayout.textSize,
    textColor = context.color(R.color.light_text),
    backgroundColor = context.color(R.color.light_control_button),
    onClicked = { onItemClicked(it) }
  )
  
  private val equalsButton = TextButton(
    context,
    text = EQUALS_SYMBOL,
    textSize = context.dimen(R.dimen.text_h0),
    textColor = context.color(R.color.light_text_light),
    backgroundColor = context.color(R.color.light_primary),
    rippleColor = context.color(R.color.light_ripple_light),
    onClicked = { onItemClicked(it) }
  )
  
  init {
    repeat(15) { i ->
      addView(TextButton(
        context,
        text = ELEMENTS[i],
        textSize = textSize,
        textColor = context.color(R.color.light_text),
        backgroundColor = context.color(R.color.light_element_button),
        onClicked = { onItemClicked(it) }
      ))
    }
    addView(plusButton)
    addView(equalsButton)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val elementSize = (MeasureSpec.getSize(widthMeasureSpec) - (elementsPadding * 7)) / 6
    setMeasuredDimension(
      resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
      resolveSize((elementSize * 3 + elementsPadding * 4), heightMeasureSpec)
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    elementSize = ((w - (elementsPadding * 7)) / 6).f
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    layoutElements(elementsPadding, 0)
    layoutElements(elementsPadding * 2 + elementSize.i, 5)
    layoutElements(elementsPadding * 3 + elementSize.i * 2, 10)
    
    plusButton.layout(
      (elementSize * 5).i + (elementsPadding * 6),
      elementsPadding,
      width - elementsPadding,
      elementsPadding + elementSize.i
    )
    equalsButton.layout(
      plusButton.left,
      plusButton.bottom + elementsPadding,
      width - elementsPadding,
      height - elementsPadding
    )
  }
  
  private fun layoutElements(top: Int, offset: Int) {
    var left = elementsPadding
    repeat(5) { i ->
      val child = getChildAt(i + offset)
      child.layout(left, top, (left + elementSize).i, top + elementSize.i)
      left += (elementSize + elementsPadding).i
    }
  }
}