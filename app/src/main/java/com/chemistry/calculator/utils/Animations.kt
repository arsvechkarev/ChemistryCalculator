package com.chemistry.calculator.utils

import android.animation.Animator

val AccelerateDecelerateInterpolator = android.view.animation.AccelerateDecelerateInterpolator()

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}