package com.chemistry.calculator.views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.utils.childWithClass

class Keyboard @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  var onItemClicked: (String) -> Unit = {}
  
  private lateinit var elementsLayout: ElementsLayout
  private lateinit var controlsLayout: ControlsLayout
  private lateinit var numbersLayout: NumbersLayout
  
  override fun onFinishInflate() {
    super.onFinishInflate()
    numbersLayout = childWithClass()
    controlsLayout = childWithClass()
    elementsLayout = childWithClass()
    numbersLayout.onItemClicked = { onItemClicked(it) }
    controlsLayout.onItemClicked = { onItemClicked(it) }
    elementsLayout.onItemClicked = { onItemClicked(it) }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    elementsLayout.measure(widthMeasureSpec, heightMeasureSpec)
    var totalHeight = elementsLayout.measuredHeight
    
    controlsLayout.measure(widthMeasureSpec, heightMeasureSpec)
    totalHeight += controlsLayout.measuredHeight
    
    numbersLayout.measure(widthMeasureSpec, heightMeasureSpec)
    totalHeight += numbersLayout.measuredHeight
    
    setMeasuredDimension(
      resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
      resolveSize(totalHeight, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    elementsLayout.layout(
      0,
      numbersLayout.measuredHeight + controlsLayout.measuredHeight,
      measuredWidth,
      measuredHeight
    )
    controlsLayout.layout(
      0, numbersLayout.measuredHeight, measuredWidth,
      numbersLayout.measuredHeight + controlsLayout.measuredHeight
    )
    numbersLayout.layout(
      0, 0, measuredWidth, numbersLayout.measuredHeight
    )
  }
}