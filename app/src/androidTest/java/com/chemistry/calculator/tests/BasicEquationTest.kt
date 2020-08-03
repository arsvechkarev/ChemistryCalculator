package com.chemistry.calculator.tests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.chemistry.calculator.MainActivity
import com.chemistry.calculator.screens.CameraScreen
import com.chemistry.calculator.screens.SolvingScreen
import com.chemistry.calculator.utils.sleep
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BasicEquationTest {
  
  @get:Rule
  val rule = ActivityTestRule(MainActivity::class.java)
  
  @Test
  fun typing_H2SO4() {
    onScreen<CameraScreen> { openKeyboardButton.click() }
    onScreen<SolvingScreen> {
      editText.hasEmptyText()
      
      button_H.click()
      button_2.click()
      button_S.click()
      button_O.click()
      button_4.click()
      
      editText.hasText("H₂SO₄")
    }
  }
}