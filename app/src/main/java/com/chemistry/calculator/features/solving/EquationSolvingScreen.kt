package com.chemistry.calculator.features.solving

import android.view.inputmethod.EditorInfo
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.views.EquationEditText
import com.chemistry.calculator.views.keyboard.Keyboard

class EquationSolvingScreen(
  equationEditText: EquationEditText,
  keyboard: Keyboard
) {
  
  private val solver = Solver()
  
  private val keyboardInput = KeyboardInput(
    AndroidInputConnection(equationEditText.onCreateInputConnection(EditorInfo())),
    isEditTextEmpty = { equationEditText.text?.isEmpty() == true },
    onStartSolving = {
      solver.Work(equationEditText.text!!.toString(), object : ResultCallback {
        
        override fun error(message: String) {
          println("qqq: error $message")
        }
  
        override fun success(result: String) {
          println("qqq: success $result")
        }
      })
    }
  )
  
  init {
    keyboard.onItemClicked = keyboardInput::processSymbol
  }
  
  fun processEquation(equation: String) {
    keyboardInput.setEquation(equation)
  }
}