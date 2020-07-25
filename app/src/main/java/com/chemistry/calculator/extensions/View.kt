package com.chemistry.calculator.extensions

import android.view.View
import android.view.ViewGroup

fun ViewGroup.forEachChild(action: (View) -> Unit) {
  repeat(childCount) { i ->
    action(getChildAt(i))
  }
}