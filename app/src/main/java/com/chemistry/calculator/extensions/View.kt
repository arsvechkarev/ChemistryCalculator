package com.chemistry.calculator.extensions

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes

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