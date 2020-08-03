package com.chemistry.calculator.utils

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.chemistry.calculator.views.keyboard.TextButton
import org.hamcrest.Description
import org.hamcrest.Matcher

class TextButtonMatcher(
  private val stringMatcher: Matcher<String>
) : BoundedMatcher<View, TextButton>(TextButton::class.java) {
  
  override fun describeTo(description: Description) {
    description.appendText("with text: ")
    stringMatcher.describeTo(description)
  }
  
  override fun matchesSafely(item: TextButton): Boolean {
    return stringMatcher.matches(item.getText())
  }
}