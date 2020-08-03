package com.chemistry.calculator.screens

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.chemistry.calculator.R

class CameraScreen : Screen<CameraScreen>() {
  
  val openKeyboardButton = KButton { withId(R.id.openKeyboardButton) }
}