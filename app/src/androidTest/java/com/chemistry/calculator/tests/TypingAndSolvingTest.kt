package com.chemistry.calculator.tests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.chemistry.calculator.MainActivity
import com.chemistry.calculator.screens.CameraScreen
import com.chemistry.calculator.screens.SolvingScreen
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TypingAndSolvingTest {
  
  @get:Rule
  val rule = ActivityTestRule(MainActivity::class.java)
  
  @Test
  fun test1_Typing_H2SO4_and_checking_edit_text() {
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
  
  @Test
  fun test2_Typing_H2SO4_plus_NA_and_checking_result() {
    onScreen<CameraScreen> { openKeyboardButton.click() }
    onScreen<SolvingScreen> {
      editText.hasEmptyText()
      
      button_H.click()
      button_2.click()
      button_S.click()
      button_O.click()
      button_4.click()
      buttonPlus.click()
      button_Na.click()
      
      editText.hasText("H₂SO₄ + Na")
      
      buttonSolve.click()
      
      textSolution.hasText("H₂SO₄ + 2Na → Na₂SO₄ + H₂")
      
      repeat(7) {
        buttonBackspace.click()
      }
      
      editText.hasEmptyText()
    }
  }
}