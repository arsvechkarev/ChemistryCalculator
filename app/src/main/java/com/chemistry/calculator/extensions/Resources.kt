package com.chemistry.calculator.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
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

fun Context.color(@ColorRes resId: Int): Int {
  return ResourcesCompat.getColor(resources, resId, theme)
}

fun Context.attrColor(@ColorRes resId: Int): Int {
  return color(resId)
//  val typedValue = TypedValue()
//  val resolved = theme.resolveAttribute(resId, typedValue, false)
//  assertThat(resolved) { "Attribute cannot be resolved" }
//  return typedValue.data
}
