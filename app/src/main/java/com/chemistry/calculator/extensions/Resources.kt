package com.chemistry.calculator.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.res.ResourcesCompat
import com.chemistry.calculator.Application

val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

val Int.dp get() = this * Application.density
val Float.dp get() = this * Application.density
val Int.dpInt get() = (this * Application.density).toInt()

fun Context.dimen(@DimenRes resId: Int): Float {
  return resources.getDimension(resId)
}

fun Context.retrieveColor(@ColorRes resId: Int): Int {
  return ResourcesCompat.getColor(resources, resId, theme)
}