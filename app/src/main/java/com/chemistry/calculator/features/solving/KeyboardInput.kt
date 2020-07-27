package com.chemistry.calculator.features.solving

import androidx.core.text.HtmlCompat
import com.chemistry.calculator.core.DELETE_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.core.SPACE_HTML_SYMBOL
import com.chemistry.calculator.core.inputconnection.InputConnectionInterface
import com.chemistry.calculator.extensions.isDigit
import com.chemistry.calculator.extensions.isLetter
import com.chemistry.calculator.extensions.isLowercaseLetter
import com.chemistry.calculator.extensions.isNotBracket
import com.chemistry.calculator.extensions.isNotLetter
import com.chemistry.calculator.extensions.isNotSubscriptNumber
import com.chemistry.calculator.extensions.isOpenBracket
import com.chemistry.calculator.extensions.isPlus
import com.chemistry.calculator.extensions.isSpace
import com.chemistry.calculator.extensions.isSubscriptNumber
import com.chemistry.calculator.extensions.toSubscriptDigit

class KeyboardInput(
  private val inputConnection: InputConnectionInterface,
  private var isEditTextEmpty: () -> Boolean
) {
  
  private var smartDeletionMode = true
  
  fun processSymbol(symbol: String) {
    when {
      symbol == DELETE_SYMBOL -> handleDeleteSymbol()
      symbol == PLUS_SYMBOL -> processPlusSymbol()
      symbol.isDigit -> processDigit(symbol)
      else -> inputConnection.commitText(symbol)
    }
  }
  
  fun setEquation(equation: String) {
    smartDeletionMode = false
    inputConnection.commitText(equation)
  }
  
  private fun handleDeleteSymbol() {
    val selectedText: CharSequence? = inputConnection.getSelectedText()
    if (selectedText.isNullOrEmpty()) {
      if (smartDeletionMode) {
        val afterText = inputConnection.getTextAfterCursor(1)
        val beforeText = inputConnection.getTextBeforeCursor(1)
        if (tryHandleMiddleElementDeletion(afterText)) return
        if (tryHandleElementDeletion(beforeText)) return
        if (tryDeletePlusSign(beforeText, afterText)) return
      }
      
      inputConnection.deleteSurroundingText(1, 0)
    } else {
      // Delete the selection
      inputConnection.commitText("")
    }
    if (isEditTextEmpty()) {
      smartDeletionMode = true
    }
  }
  
  private fun tryHandleElementDeletion(beforeText: CharSequence): Boolean {
    if (beforeText.isLetter) {
      if (beforeText.isLowercaseLetter) {
        // Trying to delete 2-symbols element
        inputConnection.deleteSurroundingText(2, 0)
      } else {
        inputConnection.deleteSurroundingText(1, 0)
      }
      deleteAllLeftSubscriptNumbers()
      return true
    }
    return false
  }
  
  private fun tryHandleMiddleElementDeletion(afterText: CharSequence): Boolean {
    if (afterText.isLowercaseLetter) {
      inputConnection.deleteSurroundingText(1, 1)
      deleteAllLeftSubscriptNumbers()
      return true
    }
    return false
  }
  
  private fun tryDeletePlusSign(beforeText: CharSequence, afterText: CharSequence): Boolean {
    if (beforeText.isSpace) {
      if (afterText.isPlus) {
        // ..._|+_...
        inputConnection.deleteSurroundingText(1, 2)
      } else if (!afterText.isSpace) {
        // ..._+_|...
        inputConnection.deleteSurroundingText(3, 0)
      }
      return true
    } else if (beforeText.isPlus) {
      // ..._+|_...
      inputConnection.deleteSurroundingText(2, 1)
      return true
    }
    return false
  }
  
  private fun deleteAllLeftSubscriptNumbers() {
    while (inputConnection.getTextAfterCursor(1).isSubscriptNumber) {
      inputConnection.deleteSurroundingText(0, 1)
    }
  }
  
  private fun processPlusSymbol() {
    val text = HtmlCompat.fromHtml(
      "$SPACE_HTML_SYMBOL$PLUS_SYMBOL$SPACE_HTML_SYMBOL", HtmlCompat.FROM_HTML_MODE_COMPACT
    )
    inputConnection.commitText(text)
  }
  
  private fun processDigit(symbol: String) {
    val beforeText = inputConnection.getTextBeforeCursor(1)
    val afterText = inputConnection.getTextAfterCursor(1)
    if (beforeText.isOpenBracket) return
    if (afterText.isLowercaseLetter) return
    if (beforeText.isNotLetter && beforeText.isNotBracket && beforeText.isNotSubscriptNumber) {
      inputConnection.commitText(symbol)
    } else {
      inputConnection.commitText(symbol.toSubscriptDigit())
    }
  }
}