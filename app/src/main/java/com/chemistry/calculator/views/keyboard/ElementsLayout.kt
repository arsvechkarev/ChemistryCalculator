package com.chemistry.calculator.views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.core.ELEMENTS
import com.chemistry.calculator.core.EQUALS_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.extensions.color
import com.chemistry.calculator.extensions.dimen
import com.chemistry.calculator.extensions.f
import com.chemistry.calculator.extensions.i

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
    backgroundColor = context.color(R.color.light_equals_button),
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
    var left = elementsPadding // TODO (7/26/2020): Optimize cycles
    var top = elementsPadding
    repeat(5) {
      val child = getChildAt(it)
      child.layout(left, top, (left + elementSize).i, top + elementSize.i)
      left += (elementSize + elementsPadding).i
    }
    left = elementsPadding
    top = elementsPadding * 2 + elementSize.i
    repeat(5) {
      val child = getChildAt(it + 5)
      child.layout(left, top, (left + elementSize).i, top + elementSize.i)
      left += (elementSize + elementsPadding).i
    }
    left = elementsPadding
    top = elementsPadding * 3 + elementSize.i * 2
    repeat(5) {
      val child = getChildAt(it + 10)
      child.layout(left, top, (left + elementSize).i, top + elementSize.i)
      left += (elementSize + elementsPadding).i
    }
    
    getChildAt(14).bottom
    measuredHeight
    elementsPadding
    
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
}