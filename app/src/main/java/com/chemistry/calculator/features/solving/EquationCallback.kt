package com.chemistry.calculator.features.solving

/**
 * Callback for equation solving
 */
interface EquationCallback {
  
  fun success(result: String)
  
  fun error(message: String)
}