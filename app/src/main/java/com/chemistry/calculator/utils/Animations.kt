package com.chemistry.calculator.utils

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

fun View.animateInvisible(andThen: () -> Unit = {}) {
  if (visibility == INVISIBLE) {
    andThen()
    return
  }
  animate()
      .alpha(0f)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .setDuration(DURATION_SMALL)
      .doOnEnd {
        invisible()
        alpha = 1f
        andThen()
      }
      .start()
}

fun View.makeVisibleAndMove(onStart: () -> Unit = {}, onEnd: () -> Unit = {}) {
  if (visibility == VISIBLE) {
    animateInvisible(andThen = { makeVisibleAndMove(onStart, onEnd) })
    return
  }
  onStart()
  alpha = 0f
  visible()
  translationY = -height / 2f
  animate()
      .setInterpolator(AccelerateDecelerateInterpolator)
      .setDuration(DURATION_DEFAULT)
      .alpha(1f)
      .translationY(0f)
      .doOnEnd { onEnd() }
      .start()
}
