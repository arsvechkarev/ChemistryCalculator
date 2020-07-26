package com.chemistry.calculator.extensions

import org.junit.Assert.*
import org.junit.Test

class CharSequenceKtTest {
  
  @Test
  fun `Subscript number to normal number conversion`() {
    val sub2 = "₂"
    val normal5 = "5"
    
    assertTrue(sub2.toNormalDigit() == "2")
    assertTrue(normal5.toSubscriptDigit() == "₅")
  }
}