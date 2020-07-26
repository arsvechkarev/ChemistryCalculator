package com.chemistry.calculator.features.solving

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.views.keyboard.Keyboard

class EquationSolvingScreen(
  editText: EditText,
  keyboard: Keyboard
) {
  
  private val keyboardInput = KeyboardInput(
    AndroidInputConnection(editText.onCreateInputConnection(EditorInfo()))
  )
  
  init {
    editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
    editText.setTextIsSelectable(true)
    keyboard.onItemClicked = keyboardInput::processSymbol
  }
  
  fun processEquation(equation: String) {
  
  }
}