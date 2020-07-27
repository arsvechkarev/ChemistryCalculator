package com.chemistry.calculator.extensions


val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

fun Int.coerceInFloat(minimumValue: Float, maximumValue: Float): Int {
  return coerceIn(minimumValue.i, maximumValue.i)
}