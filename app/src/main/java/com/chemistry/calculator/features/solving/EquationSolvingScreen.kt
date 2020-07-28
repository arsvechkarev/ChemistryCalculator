package com.chemistry.calculator.features.solving

import android.view.inputmethod.EditorInfo
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.views.EquationEditText
import com.chemistry.calculator.views.keyboard.Keyboard
import timber.log.Timber

class EquationSolvingScreen(
  equationEditText: EquationEditText,
  keyboard: Keyboard
) {
  
  private val solver = EquationSolver(object : EquationCallback {
    override fun success(result: String) {
      Timber.d(result)
    }
    
    override fun error(message: String) {
      Timber.d(message)
    }
  })
  
  private val keyboardInput = KeyboardInput(
    AndroidInputConnection(equationEditText.onCreateInputConnection(EditorInfo())),
    isEditTextEmpty = { equationEditText.text?.isEmpty() == true },
    onEqualsClicked = { solver.process(equationEditText.text!!.toString()) }
  )
  
  init {
    keyboard.onItemClicked = keyboardInput::processSymbol
  }
  
  fun processEquation(equation: String) {
    keyboardInput.setEquation(equation)
  }
}