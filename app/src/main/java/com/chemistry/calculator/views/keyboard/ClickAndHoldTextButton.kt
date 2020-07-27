package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import com.chemistry.calculator.extensions.postDelayed

@SuppressLint("ViewConstructor") // Created only through code
class ClickAndHoldTextButton constructor(
  context: Context,
  text: String,
  textSize: Float,
  textColor: Int,
  backgroundColor: Int,
  onClicked: (String) -> Unit,
  private val onHold: (String) -> Unit
) : TextButton(context, text, textSize, textColor, backgroundColor, onClicked = onClicked) {
  
  private var isHoldingNow = false
  
  @SuppressLint("HandlerLeak")
  private val holdHandler = object : Handler() {
    
    override fun handleMessage(msg: Message) {
      if (isHoldingNow) {
        onHold(text)
        sendEmptyMessageDelayed(WHAT, DELAY_HOLDING)
      }
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        isHoldingNow = true
        handler.postDelayed(DELAY_START_HOLDING) { holdHandler.sendEmptyMessage(WHAT) }
      }
      ACTION_UP -> {
        isHoldingNow = false
        holdHandler.removeCallbacksAndMessages(null)
      }
    }
    return super.onTouchEvent(event)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    holdHandler.removeCallbacksAndMessages(null)
  }
  
  companion object {
    private const val WHAT = 77
    private const val DELAY_START_HOLDING = 600L
    private const val DELAY_HOLDING = 70L
  }
}