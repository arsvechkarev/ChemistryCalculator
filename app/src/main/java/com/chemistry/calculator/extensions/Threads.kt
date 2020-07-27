package com.chemistry.calculator.extensions

import android.os.Handler

fun Handler.postDelayed(delay: Long, action: () -> Unit) {
  postDelayed(action, delay)
}