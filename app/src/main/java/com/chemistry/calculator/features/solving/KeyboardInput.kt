package com.chemistry.calculator.features.solving

import androidx.core.text.HtmlCompat
import com.chemistry.calculator.core.BACKSPACE_SYMBOL
import com.chemistry.calculator.core.SOLVE_SYMBOL
import com.chemistry.calculator.core.MORE_SYMBOL
import com.chemistry.calculator.core.PLUS_SYMBOL
import com.chemistry.calculator.core.SPACE_HTML_SYMBOL
import com.chemistry.calculator.core.inputconnection.InputConnectionInterface
import com.chemistry.calculator.utils.isDigit
import com.chemistry.calculator.utils.isLetter
import com.chemistry.calculator.utils.isLowercaseLetter
import com.chemistry.calculator.utils.isNotBracket
import com.chemistry.calculator.utils.isNotLetter
import com.chemistry.calculator.utils.isNotSubscriptNumber
import com.chemistry.calculator.utils.isOpenBracket
import com.chemistry.calculator.utils.isPlus
import com.chemistry.calculator.utils.isSpace
import com.chemistry.calculator.utils.isSubscriptNumber
import com.chemistry.calculator.utils.toSubscriptDigit

class KeyboardInput(
  private val inputConnection: InputConnectionInterface,
  private val isEditTextEmpty: () -> Boolean,
  private val isCommittingAllowed: (String) -> Boolean,
  private val onMoreClicked: () -> Unit,
  private val onEqualsClicked: () -> Unit
) {
  
  private var smartErasingMode = true
  
  fun processSymbol(symbol: String) {
    when {
      symbol == MORE_SYMBOL -> onMoreClicked()
      symbol == BACKSPACE_SYMBOL -> handleDeleteSymbol()
      symbol == SOLVE_SYMBOL -> onEqualsClicked()
      symbol == PLUS_SYMBOL -> processPlusSymbol()
      symbol.isDigit -> processDigit(symbol)
      else -> commitText(symbol)
    }
  }
  
  fun setEquation(equation: String) {
    smartErasingMode = false
    commitText(equation)
    onEqualsClicked()
  }
  
  private fun handleDeleteSymbol() {
    val selectedText: CharSequence? = inputConnection.getSelectedText()
    if (selectedText.isNullOrEmpty()) {
      if (smartErasingMode) {
        val afterText = inputConnection.getTextAfterCursor(1)
        val beforeText = inputConnection.getTextBeforeCursor(1)
        if (tryHandleMiddleElementDeletion(afterText)) return
        if (tryHandleElementDeletion(beforeText)) return
        if (tryDeletePlusSign(beforeText, afterText)) return
      }
      
      inputConnection.deleteSurroundingText(1, 0)
    } else {
      // Delete the selection
      commitText("")
    }
    if (isEditTextEmpty()) {
      smartErasingMode = true
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
      } else {
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
    commitText(text)
  }
  
  private fun processDigit(symbol: String) {
    val beforeText = inputConnection.getTextBeforeCursor(1)
    val afterText = inputConnection.getTextAfterCursor(1)
    if (beforeText.isOpenBracket) return
    if (afterText.isLowercaseLetter) return
    if (beforeText.isNotLetter && beforeText.isNotBracket && beforeText.isNotSubscriptNumber) {
      commitText(symbol)
    } else {
      commitText(symbol.toSubscriptDigit())
    }
  }
  
  private fun commitText(symbol: CharSequence) {
    if (isCommittingAllowed(symbol.toString())) {
      inputConnection.commitText(symbol)
    }
  }
}