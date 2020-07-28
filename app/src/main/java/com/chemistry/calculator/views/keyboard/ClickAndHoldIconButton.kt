package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import com.chemistry.calculator.utils.postDelayed

@SuppressLint("ViewConstructor") // Created only through code
class ClickAndHoldIconButton constructor(
  context: Context,
  id: String,
  iconRes: Int,
  iconColor: Int,
  backgroundColor: Int,
  onClicked: (String) -> Unit,
  private val onHold: (String) -> Unit
) : IconButton(context, id, iconRes, iconColor, backgroundColor, onClicked = onClicked) {
  
  private var isHoldingNow = false
  
  @SuppressLint("HandlerLeak")
  private val holdHandler = object : Handler() {
    
    override fun handleMessage(msg: Message) {
      if (isHoldingNow) {
        onHold(id)
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