package com.chemistry.calculator.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.StateSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun forViews(vararg views: View, action: (View) -> Unit) {
  for (view in views) action(view)
}

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

fun ViewGroup.addViews(vararg views: View) {
  for (view in views) {
    addView(view)
  }
}

fun createClickableBackground(cornersRadius: Float, backgroundColor: Int, rippleColor: Int): Drawable {
  val roundCornersShape = RoundRectShape(floatArrayOf(cornersRadius, cornersRadius, cornersRadius,
    cornersRadius, cornersRadius, cornersRadius, cornersRadius, cornersRadius), null, null)
  val backgroundDrawable = ShapeDrawable().apply {
    shape = roundCornersShape
    paint.color = backgroundColor
  }
  val maskDrawable = ShapeDrawable().apply {
    shape = roundCornersShape
    paint.color = rippleColor
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    return RippleDrawable(ColorStateList.valueOf(rippleColor), backgroundDrawable, maskDrawable)
  } else {
    return StateListDrawable().apply {
      addState(intArrayOf(android.R.attr.state_pressed), maskDrawable)
      addState(StateSet.WILD_CARD, backgroundDrawable)
    }
  }
}

@OptIn(ExperimentalContracts::class)
inline fun View.withParams(block: View.(ViewGroup.MarginLayoutParams) -> Unit) {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  block.invoke(this, layoutParams as ViewGroup.MarginLayoutParams)
}

fun View.layoutView(left: Int, top: Int, right: Int, bottom: Int) {
  layout(left, top, right, bottom)
}
