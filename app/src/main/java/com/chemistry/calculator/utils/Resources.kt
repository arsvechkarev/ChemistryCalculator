package com.chemistry.calculator.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.res.ResourcesCompat
import com.chemistry.calculator.core.Application

fun Context.dimen(@DimenRes resId: Int): Float {
  return resources.getDimension(resId)
}

fun Context.color(@ColorRes resId: Int): Int {
  return ResourcesCompat.getColor(resources, resId, theme)
}