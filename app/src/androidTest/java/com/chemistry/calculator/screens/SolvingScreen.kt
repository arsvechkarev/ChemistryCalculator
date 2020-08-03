package com.chemistry.calculator.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.chemistry.calculator.R
import com.chemistry.calculator.core.BACKSPACE_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.core.SOLVE_SYMBOL
import com.chemistry.calculator.utils.itemButtonWithId

@Suppress("PropertyName")
class SolvingScreen : Screen<SolvingScreen>() {
  
  val editText = KEditText { withId(R.id.equationEditText) }
  val textSolution = KTextView { withId(R.id.textSolution) }
  val textError = KTextView { withId(R.id.textError) }
  
  val button_H = KView { itemButtonWithId("H") }
  val button_S = KView { itemButtonWithId("S") }
  val button_O = KView { itemButtonWithId("O") }
  val button_Na = KView { itemButtonWithId("Na") }
  val button_C = KView { itemButtonWithId("C") }
  val button_Be = KView { itemButtonWithId("Be") }
  
  val button_2 = KView { itemButtonWithId("2") }
  val button_4 = KView { itemButtonWithId("4") }
  
  val buttonPlus = KView { itemButtonWithId(PLUS_SYMBOL) }
  val buttonSolve = KView { itemButtonWithId(SOLVE_SYMBOL) }
  val buttonBackspace = KView { itemButtonWithId(BACKSPACE_SYMBOL) }
}
