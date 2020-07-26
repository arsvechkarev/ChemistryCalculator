package com.chemistry.calculator.extensions

import android.graphics.Matrix
import android.graphics.Rect
import android.text.BoringLayout
import android.text.Layout
import android.text.TextPaint
import android.text.TextUtils

val tempRect = Rect()
val tempMatrix = Matrix()

fun boringLayoutOf(textPaint: TextPaint, text: CharSequence, maxWidth: Float = -1f): Layout {
  val metrics = BoringLayout.isBoring(text, textPaint)
  if (maxWidth == -1f) {
    return BoringLayout.make(text, textPaint, metrics.width,
      Layout.Alignment.ALIGN_CENTER, 0f, 0f, metrics, false)
  } else {
    return BoringLayout.make(text, textPaint, metrics.width,
      Layout.Alignment.ALIGN_CENTER, 0f, 0f, metrics, false,
      TextUtils.TruncateAt.END, maxWidth.toInt())
  }
}