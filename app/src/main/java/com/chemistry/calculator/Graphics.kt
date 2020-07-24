package com.chemistry.calculator

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import androidx.camera.core.ImageProxy

private val tempMatrix = Matrix()

fun ImageProxy.toBitmap(): Bitmap {
  val buffer = planes[0].buffer
  buffer.rewind()
  val bytes = ByteArray(buffer.capacity())
  buffer.get(bytes)
  return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.rotate(): Bitmap {
  tempMatrix.reset()
  tempMatrix.setRotate(90f, 0.5f, 0.5f)
  return Bitmap.createBitmap(this, 0, 0, width, height, tempMatrix, false)
}

fun Bitmap.crop(rect: Rect, boxView: BoxView): Bitmap {
  val widthCoefficient = this.width.toFloat() / boxView.width
  val heightCoefficient = this.height.toFloat() / boxView.height
  val x = (rect.left * widthCoefficient).toInt()
  val y = (rect.top * heightCoefficient).toInt()
  val width = (rect.width() * widthCoefficient).toInt()
  val height = (rect.height() * heightCoefficient).toInt()
  return Bitmap.createBitmap(this, x, y, width, height)
}
