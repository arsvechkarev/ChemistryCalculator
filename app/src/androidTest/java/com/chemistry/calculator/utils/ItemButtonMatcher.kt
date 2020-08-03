package com.chemistry.calculator.utils

import android.view.View
import com.chemistry.calculator.views.keyboard.ItemButton
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class ItemButtonMatcher(
  private val stringMatcher: Matcher<String>
) : BaseMatcher<View>() {
  
  override fun describeTo(description: Description) {
    description.appendText("with item id: ")
    stringMatcher.describeTo(description)
  }
  
  override fun matches(item: Any?): Boolean {
    if (item == null) return false
    if (item !is ItemButton) return false
    return stringMatcher.matches(item.itemId)
  }
}