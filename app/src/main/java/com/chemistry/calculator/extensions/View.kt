package com.chemistry.calculator.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes

fun ViewGroup.forEachChild(action: (View) -> Unit) {
  repeat(childCount) { i ->
    action(getChildAt(i))
  }
}

fun ViewGroup.forEachChildIndexed(action: (Int, View) -> Unit) {
  repeat(childCount) { i ->
    action(i, getChildAt(i))
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

fun ViewGroup.childWithId(@IdRes id: Int): View {
  return findChild { child -> child.id == id }
}

inline fun <reified T : View> ViewGroup.childWithClass(): T {
  return findChild { child -> child is T } as T
}
