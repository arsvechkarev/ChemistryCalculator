package com.chemistry.calculator.tests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.chemistry.calculator.MainActivity
import com.chemistry.calculator.screens.SolvingScreen
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DetectingFailuresTest {
  
  @get:Rule
  val rule = ActivityTestRule(MainActivity::class.java)
  
  @Test
  fun test1_Detecting_input_error() {
    onScreen<SolvingScreen> {
      editText.hasEmptyText()
      
      button_Be.click()
      button_C.click()
      
      editText.hasText("BeC")
      
      buttonSolve.click()
  
      textError.isDisplayed()
      textSolution.isNotDisplayed()
    }
  }
}