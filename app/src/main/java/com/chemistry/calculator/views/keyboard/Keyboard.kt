package com.chemistry.calculator.views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.chemistry.calculator.R
import com.chemistry.calculator.core.BACKSPACE_SYMBOL
import com.chemistry.calculator.core.CLOSE_BRACKET_SYMBOL
import com.chemistry.calculator.core.DOUBLE_BOND_SYMBOL
import com.chemistry.calculator.core.ELEMENTS
import com.chemistry.calculator.core.ELEMENTS_2
import com.chemistry.calculator.core.EQUALS_SYMBOL
import com.chemistry.calculator.core.MORE_SYMBOL
import com.chemistry.calculator.core.OPEN_BRACKET_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.core.SINGULAR_BOND_KEYBOARD_SYMBOL
import com.chemistry.calculator.core.TRIPLE_BOND_SYMBOL
import com.chemistry.calculator.utils.addViews
import com.chemistry.calculator.utils.ceilInt
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.dimen
import com.chemistry.calculator.utils.forViews
import com.chemistry.calculator.utils.i
import com.chemistry.calculator.views.keyboard.CombinedButton.MODE.TEXT
import kotlin.math.ceil

class Keyboard @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
  
  private val numberTextSize = context.dimen(R.dimen.text_h2)
  private val textSize = context.dimen(R.dimen.text_h1)
  private val textColor = context.color(R.color.light_text)
  private val elementBackgroundColor = context.color(R.color.light_element_button)
  private val controlBackgroundColor = context.color(R.color.light_control_button)
  private val elementsPadding = context.dimen(R.dimen.keyboard_elements_padding)
  
  private var numberHeight = -1f
  private var numberWidth = -1f
  private var controlsHeight = -1f
  private var controlsWidth = -1f
  private var elementSize = -1f
  
  private var isShowingPrimaryElements = true
  
  var onItemClicked: (String) -> Unit = {}
  
  private val equalsButton = TextButton(
    context, EQUALS_SYMBOL, context.dimen(R.dimen.text_h0),
    context.color(R.color.light_text_light), context.color(R.color.light_primary),
    rippleColor = context.color(R.color.light_ripple_light), onClicked = { processItem(it) }
  )
  
  private val moreButton = TextButton(context, MORE_SYMBOL, context.dimen(R.dimen.text_h0), textColor,
    controlBackgroundColor, onClicked = { processItem(it) })
  
  private val combinedButton1 = CombinedButton(
    context, R.drawable.ic_singular_bond, SINGULAR_BOND_KEYBOARD_SYMBOL,
    OPEN_BRACKET_SYMBOL, textSize,
    textColor, controlBackgroundColor,
    currentMode = TEXT,
    drawWithDescent = true,
    onClicked = { processItem(it) }
  )
  
  private val combinedButton2 = CombinedButton(
    context, R.drawable.ic_double_bond, DOUBLE_BOND_SYMBOL,
    CLOSE_BRACKET_SYMBOL, textSize,
    textColor, controlBackgroundColor,
    currentMode = TEXT,
    drawWithDescent = true,
    onClicked = { processItem(it) }
  )
  
  private val combinedButton3 = CombinedButton(
    context, R.drawable.ic_triple_bond, TRIPLE_BOND_SYMBOL,
    PLUS_SYMBOL, textSize,
    textColor, controlBackgroundColor,
    currentMode = TEXT,
    onClicked = { processItem(it) }
  )
  
  private val backspaceButton = ClickAndHoldIconButton(context, BACKSPACE_SYMBOL, R.drawable.ic_backspace,
    textColor, controlBackgroundColor, onClicked = { processItem(it) }, onHold = { processItem(it) })
  
  init {
    repeat(20) { i ->
      addView(TextButton(
        context, ELEMENTS[i], textSize, textColor,
        elementBackgroundColor, onClicked = { processItem(it) }
      ))
    }
    
    addView(TextButton(
      context, ELEMENTS[20], textSize, textColor,
      elementBackgroundColor, onClicked = { processItem(it) }
    ))
    
    addView(TextButton(
      context, ELEMENTS[21], textSize, textColor,
      elementBackgroundColor, onClicked = { processItem(it) }
    ))
    
    addViews(equalsButton, combinedButton3, moreButton, combinedButton1, combinedButton2, backspaceButton)
    
    repeat(10) { i ->
      addView(TextButton(
        context, ((i + 1) % 10).toString(), numberTextSize, textColor,
        controlBackgroundColor, onClicked = { processItem(it) }
      ))
    }
  }
  
  fun toggleMoreButton() {
    combinedButton1.toggleMode()
    combinedButton2.toggleMode()
    combinedButton3.toggleMode()
    
    if (isShowingPrimaryElements) {
      changeText(ELEMENTS_2, View.INVISIBLE)
    } else {
      changeText(ELEMENTS, View.VISIBLE)
    }
    isShowingPrimaryElements = !isShowingPrimaryElements
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val elementWidth = (width - (elementsPadding * 7)) / 6
    val numberHeight = numberTextSize.i * 2
    val sum = elementWidth * 5 + numberHeight + elementsPadding * 7
    setMeasuredDimension(
      resolveSize(width, widthMeasureSpec),
      resolveSize(ceil(sum).toInt(), heightMeasureSpec)
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    elementSize = (w - elementsPadding * 7) / 6
    numberHeight = numberTextSize * 2
    numberWidth = (w - elementsPadding * 11) / 10
    controlsWidth = w / 5f
    controlsHeight = elementSize
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val startTop = numberHeight + controlsHeight + elementsPadding * 3
    layoutElements(startTop, 0)
    layoutElements(startTop + elementSize + elementsPadding, 5)
    layoutElements(startTop + elementSize * 2 + elementsPadding * 2, 10)
    layoutElements(startTop + elementSize * 3 + elementsPadding * 3, 15)
    
    val edgeElement = getChildAt(4)
    val paddingInt = elementsPadding.ceilInt()
    val elementSizeInt = elementSize.toInt()
    equalsButton.layout(
      edgeElement.right + paddingInt,
      (height - elementSize * 2 - paddingInt * 2).toInt(),
      width - paddingInt,
      height - paddingInt
    )
    
    val element20 = getChildAt(20)
    element20.layout(
      equalsButton.left,
      equalsButton.top - elementSizeInt - paddingInt,
      equalsButton.right,
      equalsButton.top - paddingInt
    )
    
    getChildAt(21).layout(
      element20.left,
      element20.top - elementSizeInt - paddingInt,
      element20.right,
      element20.top - paddingInt
    )
    
    val firstChild = getChildAt(0)
    moreButton.layout(
      paddingInt, firstChild.top - elementSizeInt,
      paddingInt + elementSizeInt, firstChild.top - paddingInt
    )
    
    var left = moreButton.right + paddingInt
    forViews(combinedButton1, combinedButton2, combinedButton3, backspaceButton) { view ->
      view.layout(left, moreButton.top, (left + controlsWidth).i, moreButton.bottom)
      left += controlsWidth.toInt() + paddingInt
    }
    
    var right = width - elementsPadding
    repeat(10) { i ->
      val child = getChildAt(childCount - i - 1)
      child.layout(
        (right - numberWidth).i,
        paddingInt,
        right.i,
        combinedButton1.top - paddingInt
      )
      right -= numberWidth + elementsPadding
    }
  }
  
  private fun changeText(elementsText: Array<String>, viewVisibility: Int) {
    repeat(18) { i ->
      val textButton = getChildAt(i) as TextButton
      textButton.updateText(elementsText[i])
    }
    getChildAt(18).visibility = viewVisibility
    getChildAt(19).visibility = viewVisibility
    (getChildAt(20) as TextButton).updateText(elementsText[elementsText.size - 2])
    (getChildAt(21) as TextButton).updateText(elementsText.last())
  }
  
  private fun layoutElements(top: Float, offset: Int) {
    var left = elementsPadding.i
    repeat(5) { i ->
      val child = getChildAt(i + offset)
      child.layout(left, top.i, left + elementSize.i, (top + elementSize).i)
      left += (elementSize + elementsPadding).i
    }
  }
  
  private fun processItem(item: String) {
    if (item != BACKSPACE_SYMBOL) {
      backspaceButton.onItemClicked()
    }
    onItemClicked(item)
  }
}
