package com.chemistry.calculator.utils

import android.os.Handler

fun Handler.postDelayed(delay: Long, action: () -> Unit) {
  postDelayed(action, delay)
}