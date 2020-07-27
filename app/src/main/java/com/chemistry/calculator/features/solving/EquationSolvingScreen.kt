package com.chemistry.calculator.features.solving

import android.view.inputmethod.EditorInfo
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.views.EquationEditText
import com.chemistry.calculator.views.keyboard.Keyboard

class EquationSolvingScreen(
  equationEditText: EquationEditText,
  keyboard: Keyboard
) {
  
  private val keyboardInput = KeyboardInput(
    AndroidInputConnection(equationEditText.onCreateInputConnection(EditorInfo())),
    isEditTextEmpty = { equationEditText.text?.isEmpty() == true }
  )
  
  init {
    keyboard.onItemClicked = keyboardInput::processSymbol
  }
  
  fun processEquation(equation: String) {
    keyboardInput.setEquation(equation)
  }
}