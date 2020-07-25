package com.chemistry.calculator.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.chemistry.calculator.extensions.childWithClass

class KeyboardLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val elementsLayout = childWithClass<ElementsLayout>()
    val controlsLayout = childWithClass<ControlsLayout>()
    val numbersLayout = childWithClass<NumbersLayout>()
    
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
    val elementsLayout = childWithClass<ElementsLayout>()
    val controlsLayout = childWithClass<ControlsLayout>()
    val numbersLayout = childWithClass<NumbersLayout>()
    
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