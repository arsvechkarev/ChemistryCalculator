package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP

@SuppressLint("ViewConstructor") // Created only through code
class ClickAndHoldIconButton constructor(
  context: Context,
  private val id: String,
  iconRes: Int,
  iconColor: Int,
  backgroundColor: Int,
  onClicked: (String) -> Unit,
  private val onDown: (String) -> Unit
) : IconButton(context, id, iconRes, iconColor, backgroundColor, onClicked = onClicked) {
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        onDown(id)
      }
    }
    return super.onTouchEvent(event)
  }
}