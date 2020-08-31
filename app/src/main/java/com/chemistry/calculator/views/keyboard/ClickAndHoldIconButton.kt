package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.ViewConfiguration
import com.chemistry.calculator.utils.postDelayed
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.hypot

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
  
  private val initiatorHandler = Handler()
  private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop * 2
  
  private var isHoldingNow = AtomicBoolean(false)
  private var lastDownPoint = PointF()
  
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
        lastDownPoint.set(event.x, event.y)
        initiatorHandler.postDelayed(DELAY_START_HOLDING) { holdHandler.sendEmptyMessage(WHAT) }
      }
      ACTION_MOVE -> {
        val distX = event.x - lastDownPoint.x
        val distY = event.y - lastDownPoint.y
        val dist = hypot(distX, distY)
        if (dist > touchSlop) {
          cancelHolding()
        }
      }
      ACTION_UP, ACTION_CANCEL -> cancelHolding()
    }
    return super.onTouchEvent(event)
  }
  
  private fun cancelHolding() {
    isHoldingNow.set(false)
    holdHandler.sendEmptyMessage(WHAT)
    initiatorHandler.removeCallbacksAndMessages(null)
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