package com.chemistry.calculator.views.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.chemistry.calculator.R
import com.chemistry.calculator.core.ELEMENT_BUTTON_CORNERS_COEFFICIENT
import com.chemistry.calculator.utils.color
import com.chemistry.calculator.utils.createClickableBackground

@SuppressLint("ViewConstructor")
open class IconButton constructor(
  context: Context,
  private val id: String,
  @DrawableRes private val iconRes: Int,
  iconColor: Int,
  private val backgroundColor: Int,
  private val rippleColor: Int = context.color(R.color.light_ripple),
  var onClicked: (String) -> Unit = {}
) : View(context), ItemButton {
  
  private val drawable = ContextCompat.getDrawable(context, iconRes)!!
  private var cornersRadius = -1f
  
  init {
    setOnClickListener { onClicked(id) }
    drawable.colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
  }
  
  override val itemId: String
    get() = id
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    cornersRadius = minOf(w, h) / ELEMENT_BUTTON_CORNERS_COEFFICIENT
    val iconSize = minOf(w, h) / 2
    drawable.setBounds(
      w / 2 - iconSize / 2,
      h / 2 - iconSize / 2,
      w / 2 + iconSize / 2,
      h / 2 + iconSize / 2
    )
    background = createClickableBackground(cornersRadius, backgroundColor, rippleColor)
  }
  
  override fun onDraw(canvas: Canvas) {
    drawable.draw(canvas)
  }
}