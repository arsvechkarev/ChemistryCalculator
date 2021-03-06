package com.chemistry.calculator.features.solving

import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.utils.isBracket
import com.chemistry.calculator.utils.isSubscriptNumber
import com.chemistry.calculator.utils.toNormalDigit
import timber.log.Timber

class EquationSolver(
  private var onSuccess: (String) -> Unit,
  private var onError: (String) -> Unit
) {
  
  private val hardcoreSolver = HardcoreSolver(object : SolvingCallback {
    
    override fun success(result: String) {
      val transformResult = transformResult(result)
      onSuccess(transformResult)
    }
    
    override fun error(message: String) {
      onError(message)
    }
  })
  
  fun startSolving(equation: String) {
    val transformedEquation = transformEquation(equation)
    hardcoreSolver.process(transformedEquation)
  }
  
  private fun transformEquation(equation: String): String {
    val result = StringBuilder(equation.length)
    for (c in equation) {
      when {
        c.isBracket -> result.append(c)
        c.toString() == PLUS_SYMBOL -> result.append(c)
        c.isSubscriptNumber -> result.append(c.toString().toNormalDigit())
        c.isLetter() -> result.append(c)
      }
    }
    return result.toString()
  }
  
  private fun transformResult(result: String): String {
    // TODO (7/29/2020): implement this
    return result
  }
}