package com.chemistry.calculator.extensions

import android.animation.Animator

val AccelerateDecelerateInterpolator = android.view.animation.AccelerateDecelerateInterpolator()

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}