package com.chemistry.calculator.features.solving

import android.view.inputmethod.EditorInfo
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.views.EquationEditText
import com.chemistry.calculator.views.keyboard.Keyboard
import timber.log.Timber

class SolvingScreen(
  equationEditText: EquationEditText,
  keyboard: Keyboard
) {
  
  private val solver = EquationSolver(onSuccess = {
    Timber.d(it)
  }, onError = {
    Timber.d(it)
  })
  
  private val keyboardInput = KeyboardInput(
    AndroidInputConnection(equationEditText.onCreateInputConnection(EditorInfo())),
    isEditTextEmpty = { equationEditText.text?.isEmpty() == true },
    onMoreClicked = { keyboard.toggleMoreButton() },
    isCommittingAllowed = { symbolToAdd ->
      val newText = (equationEditText.text?.toString() ?: "") + symbolToAdd
      val textWidth = equationEditText.paint.measureText(newText)
      return@KeyboardInput textWidth / equationEditText.width < 0.9f
    },
    onEqualsClicked = { solver.startSolving(equationEditText.text!!.toString()) }
  )
  
  init {
    equationEditText.requestFocus()
    keyboard.onItemClicked = keyboardInput::processSymbol
  }
  
  fun processEquation(equation: String) {
    keyboardInput.setEquation(equation)
  }
}