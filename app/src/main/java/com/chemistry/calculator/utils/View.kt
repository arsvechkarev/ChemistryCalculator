package com.chemistry.calculator.utils

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

fun ViewGroup.forEachChild(action: (View) -> Unit) {
  repeat(childCount) { i ->
    action(getChildAt(i))
  }
}

inline fun ViewGroup.findChild(predicate: (View) -> Boolean): View {
  for (i in 0 until childCount) {
    val child = getChildAt(i)
    if (predicate(child)) {
      return child
    }
  }
  throw IllegalStateException("No child matching predicate")
}

inline fun <reified T : View> ViewGroup.childWithClass(): T {
  return findChild { child -> child is T } as T
}

operator fun View.contains(ev: MotionEvent): Boolean {
  val x = ev.x
  val y = ev.y
  return x >= left && y >= top && x <= right && y <= bottom
}

fun createRoundedRipple(cornersRadius: Float, backgroundColor: Int, rippleColor: Int): RippleDrawable {
  val roundCornersShape = RoundRectShape(floatArrayOf(cornersRadius, cornersRadius, cornersRadius,
    cornersRadius, cornersRadius, cornersRadius, cornersRadius, cornersRadius), null, null)
  val backgroundDrawable = ShapeDrawable().apply {
    shape = roundCornersShape
    paint.color = backgroundColor
  }
  val maskDrawable = ShapeDrawable().apply {
    shape = roundCornersShape
    paint.color = rippleColor
  }
  return RippleDrawable(ColorStateList.valueOf(rippleColor), backgroundDrawable, maskDrawable)
}