package com.chemistry.calculator.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EquationEditText @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
  
  init {
    setRawInputType(InputType.TYPE_CLASS_TEXT)
    setTextIsSelectable(false) // For unknown reason doesn't work
    showSoftInputOnFocus = false
  }
}