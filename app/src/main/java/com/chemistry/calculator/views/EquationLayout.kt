package com.chemistry.calculator.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.chemistry.calculator.R
import com.chemistry.calculator.utils.layoutView
import com.chemistry.calculator.utils.withParams
import com.chemistry.calculator.views.keyboard.Keyboard

class EquationLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private lateinit var equationEditText: EquationEditText
  private lateinit var textSolution: TextView
  private lateinit var textError: TextView
  private lateinit var keyboard: Keyboard
  
  override fun onFinishInflate() {
    super.onFinishInflate()
    equationEditText = findViewById(R.id.equationEditText)
    textSolution = findViewById(R.id.textSolution)
    textError = findViewById(R.id.textError)
    keyboard = findViewById(R.id.keyboard)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val height = MeasureSpec.getSize(heightMeasureSpec)
    val wSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
    val hSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)
    equationEditText.measure(wSpec, hSpec)
    textSolution.measure(wSpec, hSpec)
    textError.measure(wSpec, hSpec)
    keyboard.measure(wSpec, hSpec)
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val parentWidth = width
    val parentHeight = height
    equationEditText.withParams { params ->
      layoutView(
        left = params.leftMargin,
        top = params.topMargin,
        right = parentWidth - params.rightMargin,
        bottom = params.topMargin + equationEditText.measuredHeight
      )
    }
    
    textSolution.withParams { params ->
      val top = equationEditText.bottom + params.topMargin
      layoutView(
        left = params.leftMargin,
        top = top,
        right = parentWidth - params.rightMargin,
        bottom = top + textSolution.measuredHeight
      )
    }
    
    textError.withParams { params ->
      val top = equationEditText.bottom + params.topMargin
      layoutView(
        left = params.leftMargin,
        top = top,
        right = parentWidth - params.rightMargin,
        bottom = top + textSolution.measuredHeight
      )
    }
    
    keyboard.withParams {
      layoutView(
        left = 0,
        top = parentHeight - keyboard.measuredHeight,
        right = parentWidth,
        bottom = parentHeight
      )
    }
  }
  
  override fun generateDefaultLayoutParams(): LayoutParams {
    return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }
  
  override fun generateLayoutParams(p: LayoutParams): LayoutParams {
    return MarginLayoutParams(p)
  }
  
  override fun checkLayoutParams(p: LayoutParams): Boolean {
    return p is MarginLayoutParams
  }
  
  override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }
}