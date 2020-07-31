package com.chemistry.calculator.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator

const val DURATION_DEFAULT = 300L
const val DURATION_SMALL = 150L

val AccelerateDecelerateInterpolator = android.view.animation.AccelerateDecelerateInterpolator()

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}

fun Animator.doOnEnd(block: () -> Unit) {
  addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
      removeListener(this)
    }
  })
}

fun ViewPropertyAnimator.doOnEnd(block: () -> Unit): ViewPropertyAnimator {
  setListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
    }
  })
  return this
}
