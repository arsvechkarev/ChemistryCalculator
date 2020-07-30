package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import com.chemistry.calculator.utils.postDelayed
import java.util.concurrent.atomic.AtomicBoolean

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
  
  private var isHoldingNow = AtomicBoolean(false)
  
  private val initiatorHandler = Handler()
  
  @SuppressLint("HandlerLeak")
  private val holdHandler = object : Handler() {
    
    override fun handleMessage(msg: Message) {
      if (isHoldingNow.get()) {
        onHold(id)
        sendEmptyMessageDelayed(WHAT, DELAY_HOLDING)
      }
    }
  }
  
  fun onItemClicked() {
    isHoldingNow.set(false)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        isHoldingNow.set(true)
        initiatorHandler.postDelayed(DELAY_START_HOLDING) { holdHandler.sendEmptyMessage(WHAT) }
      }
      ACTION_UP -> {
        isHoldingNow.set(false)
        holdHandler.sendEmptyMessage(WHAT)
        initiatorHandler.removeCallbacksAndMessages(null)
      }
    }
    return super.onTouchEvent(event)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    holdHandler.removeCallbacksAndMessages(null)
    initiatorHandler.removeCallbacksAndMessages(null)
  }
  
  companion object {
    private const val WHAT = 77
    private const val DELAY_START_HOLDING = 700L
    private const val DELAY_HOLDING = 80L
  }
}