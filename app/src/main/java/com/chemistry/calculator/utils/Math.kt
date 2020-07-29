package com.chemistry.calculator.utils

import kotlin.math.ceil
import kotlin.math.floor

val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

fun Float.ceilInt() = ceil(this).toInt()
fun Float.floorInt() = floor(this).toInt()