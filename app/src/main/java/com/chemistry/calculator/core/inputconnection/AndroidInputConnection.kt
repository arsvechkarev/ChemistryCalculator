package com.chemistry.calculator.core.inputconnection

import android.view.inputmethod.InputConnection

class AndroidInputConnection(
  private val inputConnection: InputConnection
) : InputConnectionInterface {
  
  override fun getTextBeforeCursor(count: Int): CharSequence {
    return inputConnection.getTextBeforeCursor(count, 0)
  }
  
  override fun getTextAfterCursor(count: Int): CharSequence {
    return inputConnection.getTextAfterCursor(count, 0)
  }
  
  override fun getSelectedText(): CharSequence? {
    return inputConnection.getSelectedText(0)
  }
  
  override fun commitText(text: CharSequence) {
    inputConnection.commitText(text, 1)
  }
  override fun commitText(text: CharSequence, cursorPosition: Int) {
    inputConnection.commitText(text, cursorPosition)
  }
  
  override fun deleteSurroundingText(beforeLength: Int, afterLength: Int) {
    inputConnection.deleteSurroundingText(beforeLength, afterLength)
  }
}