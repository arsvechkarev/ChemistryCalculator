package com.chemistry.calculator

import android.view.MotionEvent
import android.view.View

operator fun View.contains(ev: MotionEvent): Boolean {
  val x = ev.x
  val y = ev.y
  return x >= left && y >= top && x <= right && y <= bottom
}