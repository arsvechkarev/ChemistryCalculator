package com.chemistry.calculator.utils

import com.agoda.kakao.common.builders.ViewBuilder
import org.hamcrest.CoreMatchers.`is`

fun sleep(duration: Long) {
  Thread.sleep(duration)
}

fun ViewBuilder.textButtonWithText(text: String) {
  withMatcher(TextButtonMatcher(`is`(text)))
}