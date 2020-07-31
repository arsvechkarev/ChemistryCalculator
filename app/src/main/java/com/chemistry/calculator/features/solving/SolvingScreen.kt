package com.chemistry.calculator.features.solving

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.utils.animateInvisible
import com.chemistry.calculator.utils.makeVisibleAndMove
import com.chemistry.calculator.views.EquationEditText
import com.chemistry.calculator.views.keyboard.Keyboard
import timber.log.Timber

class SolvingScreen(
  equationEditText: EquationEditText,
  textSolution: TextView,
  textError: TextView,
  keyboard: Keyboard
) {
  
  private val solver = EquationSolver(onSuccess = { equation ->
    Timber.d(equation)
    keyboard.blockEqualsButton()
    textError.animateInvisible(andThen = {
      textSolution.makeVisibleAndMove(
        onStart = { textSolution.text = equation },
        onEnd = { keyboard.unblockEqualsButton() }
      )
    })
  }, onError = { message ->
    Timber.d(message)
    keyboard.blockEqualsButton()
    textSolution.animateInvisible(andThen = {
      textError.makeVisibleAndMove(
        onStart = { textError.text = message },
        onEnd = { keyboard.unblockEqualsButton() }
      )
    })
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
    onEqualsClicked = {
      solver.startSolving(equationEditText.text!!.toString())
    }
  )
  
  init {
    equationEditText.requestFocus()
    keyboard.onItemClicked = keyboardInput::processSymbol
  }
  
  fun processEquation(equation: String) {
    keyboardInput.setEquation(equation)
  }
}