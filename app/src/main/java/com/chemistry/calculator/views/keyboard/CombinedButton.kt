package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.TextPaint
import android.view.View
import com.chemistry.calculator.R
import com.chemistry.calculator.core.ELEMENT_BUTTON_CORNERS_COEFFICIENT
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.createClickableBackground
import com.chemistry.calculator.utils.tempRect
import com.chemistry.calculator.views.keyboard.CombinedButton.MODE.ICON
import com.chemistry.calculator.views.keyboard.CombinedButton.MODE.TEXT

@SuppressLint("ViewConstructor")
class CombinedButton constructor(
  context: Context,
  iconRes: Int,
  private val iconId: String,
  private val text: String,
  private val textSize: Float,
  private val foregroundColor: Int,
  private val backgroundColor: Int,
  private val drawWithDescent: Boolean = false,
  private val rippleColor: Int = context.color(R.color.light_ripple),
  private var currentMode: MODE = TEXT,
  var onClicked: (String) -> Unit = {}
) : View(context), ItemButton {
  
  private val drawable = context.getDrawable(iconRes)!!
  private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
    textAlign = Paint.Align.CENTER
    textSize = this@CombinedButton.textSize
    color = foregroundColor
  }
  
  private var cornersRadius = -1f
  private var textYTranslate = -1f
  
  init {
    setOnClickListener {
      when (currentMode) {
        TEXT -> onClicked(text)
        ICON -> onClicked(iconId)
      }
    }
    drawable.colorFilter = PorterDuffColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP)
    isClickable = true
    isFocusable = true
  }
  
  fun toggleMode() {
    currentMode = when (currentMode) {
      TEXT -> ICON
      ICON -> TEXT
    }
    invalidate()
  }
  
  override val itemId: String
    get() {
      when (currentMode) {
        ICON -> return iconId
        TEXT -> return text
      }
    }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    cornersRadius = minOf(w, h) / ELEMENT_BUTTON_CORNERS_COEFFICIENT
    textPaint.getTextBounds(text, 0, text.length, tempRect)
    textYTranslate = height / 2f + tempRect.height() / 2f
    background = createClickableBackground(cornersRadius, backgroundColor, rippleColor)
    val iconSize = minOf(w, h) / 2
    drawable.setBounds(
      w / 2 - iconSize / 2,
      h / 2 - iconSize / 2,
      w / 2 + iconSize / 2,
      h / 2 + iconSize / 2
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    when (currentMode) {
      TEXT -> {
        if (drawWithDescent) {
          canvas.drawText(text, width / 2f, textYTranslate - textPaint.descent(), textPaint)
        } else {
          canvas.drawText(text, width / 2f, textYTranslate, textPaint)
        }
      }
      ICON -> {
        drawable.draw(canvas)
      }
    }
  }
  
  enum class MODE {
    ICON, TEXT
  }
}