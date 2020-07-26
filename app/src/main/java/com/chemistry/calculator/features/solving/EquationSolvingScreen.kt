package com.chemistry.calculator.features.solving

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.chemistry.calculator.views.keyboard.Keyboard

class EquationSolvingScreen(
  private val editText: EditText,
  private val keyboard: Keyboard
) {
  
  private val inputConnection = editText.onCreateInputConnection(EditorInfo())
  
  init {
    editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
    editText.setTextIsSelectable(true)
    keyboard.inputConnection = inputConnection
  }
  
  fun processSymbol(symbol: String) {
    inputConnection.commitText(symbol, 0)
  }
}