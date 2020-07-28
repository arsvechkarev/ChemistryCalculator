package com.chemistry.calculator.utils

import android.animation.Animator

const val DURATION_DEFAULT = 400L

val AccelerateDecelerateInterpolator = android.view.animation.AccelerateDecelerateInterpolator()

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}