package com.chemistry.calculator.core.inputconnection

/**
 * Represents set of operations with cursor/keyboard
 */
interface InputConnectionInterface {
  
  /**
   * Returns [count] symbols before cursor
   */
  fun getTextBeforeCursor(count: Int): CharSequence
  
  /**
   * Returns [count] symbols after cursor
   */
  fun getTextAfterCursor(count: Int): CharSequence
  
  /**
   * Returns selected text, or null, if no text selected
   */
  fun getSelectedText(): CharSequence?
  
  /**
   * Adds text to a current sequence
   */
  fun commitText(text: CharSequence)
  
  /**
   * Adds text to a current sequence
   */
  fun commitText(text: CharSequence, cursorPosition: Int)
  
  /**
   * Deletes [beforeLength] symbols before cursor and [afterLength] after
   */
  fun deleteSurroundingText(beforeLength: Int, afterLength: Int)
}