package com.chemistry.calculator.utils

import com.chemistry.calculator.core.CLOSE_BRACKET_SYMBOL
import com.chemistry.calculator.core.OPEN_BRACKET_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.core.SUBSCRIPT_NUMBERS

val CharSequence.isLetter: Boolean
  get() = (length == 1) && (this[0].isLetter())

val CharSequence.isNotLetter: Boolean
  get() = !isLetter

val CharSequence.isLowercaseLetter: Boolean
  get() = isLetter && (this[0].isLowerCase())

val CharSequence.isDigit: Boolean
  get() = (length == 1) && (this[0].isDigit())

val CharSequence.isSpace: Boolean
  get() = (length == 1) && (this[0].isWhitespace())

val CharSequence.isPlus: Boolean
  get() = this == PLUS_SYMBOL

val CharSequence.isOpenBracket: Boolean
  get() = this == OPEN_BRACKET_SYMBOL

val CharSequence.isNotBracket: Boolean
  get() = this != OPEN_BRACKET_SYMBOL && this != CLOSE_BRACKET_SYMBOL

val CharSequence.isSubscriptNumber: Boolean
  get() = (length == 1) && this in SUBSCRIPT_NUMBERS

val CharSequence.isNotSubscriptNumber: Boolean
  get() = !isSubscriptNumber

fun CharSequence.toSubscriptDigit(): String {
  assertThat(length == 1)
  assertThat(isDigit)
  return SUBSCRIPT_NUMBERS[toString().toInt()]
}

fun CharSequence.toNormalDigit(): String {
  assertThat(length == 1)
  return SUBSCRIPT_NUMBERS.indexOf(this).toString()
}