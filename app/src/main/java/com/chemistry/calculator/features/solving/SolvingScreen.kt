package com.chemistry.calculator.features.solving

import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.chemistry.calculator.R
import com.chemistry.calculator.core.inputconnection.AndroidInputConnection
import com.chemistry.calculator.utils.animateInvisible
import com.chemistry.calculator.utils.dimen
import com.chemistry.calculator.utils.f
import com.chemistry.calculator.utils.makeVisibleAndMove
import com.chemistry.calculator.views.EquationEditText
import com.chemistry.calculator.views.keyboard.Keyboard
import timber.log.Timber

class SolvingScreen(
  equationEditText: EquationEditText,
  private val textSolution: TextView,
  private val textError: TextView,
  private val keyboard: Keyboard
) {
  
  private val solver = EquationSolver(onSuccess = ::handleSuccess, onError = ::handleFailure)
  
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
  
  private fun handleSuccess(equation: String) {
    val textSize = computeTextSize(equation)
    Timber.d(equation)
    keyboard.blockEqualsButton()
    textError.animateInvisible(andThen = {
      textSolution.makeVisibleAndMove(
        onStart = {
          textSolution.text = equation
          textSolution.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        },
        onEnd = { keyboard.unblockEqualsButton() }
      )
    })
  }
  
  private fun computeTextSize(equation: String): Float {
    val min = keyboard.context.dimen(R.dimen.text_equation_min)
    val max = keyboard.context.dimen(R.dimen.text_equation_max)
    val maxLength = 36 // Supposed max equation length
    val fraction = 1 - equation.length.f / maxLength
    return (min + (max - min) * fraction).coerceIn(min, max)
  }
  
  private fun handleFailure(message: String) {
    Timber.d(message)
    keyboard.blockEqualsButton()
    textSolution.animateInvisible(andThen = {
      textError.makeVisibleAndMove(
        onStart = { textError.text = message },
        onEnd = { keyboard.unblockEqualsButton() }
      )
    })
  }
}