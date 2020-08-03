package com.chemistry.calculator.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.chemistry.calculator.R
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.core.SOLVE_SYMBOL
import com.chemistry.calculator.utils.textButtonWithText

@Suppress("PropertyName")
class SolvingScreen : Screen<SolvingScreen>() {
  
  val editText = KEditText { withId(R.id.equationEditText) }
  
  val button_H = KView { textButtonWithText("H") }
  val button_S = KView { textButtonWithText("S") }
  val button_O = KView { textButtonWithText("O") }
  
  val button_2 = KView { textButtonWithText("2") }
  val button_4 = KView { textButtonWithText("4") }
  
  val buttonPlus = KView { textButtonWithText(PLUS_SYMBOL) }
  val buttonSolve = KView { textButtonWithText(SOLVE_SYMBOL) }
}
