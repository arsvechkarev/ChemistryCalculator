package com.chemistry.calculator.utils

import android.util.Size
import java.lang.Long.signum

object AreaComparator : Comparator<Size> {
  
  // We cast here to ensure the multiplications won't overflow
  override fun compare(lhs: Size, rhs: Size) =
      signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
}