package com.chemistry.calculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chemistry.calculator.core.Application
import com.chemistry.calculator.features.solving.SolvingScreen
import kotlinx.android.synthetic.main.activity_main.equationEditText
import kotlinx.android.synthetic.main.activity_main.keyboard
import kotlinx.android.synthetic.main.activity_main.textError
import kotlinx.android.synthetic.main.activity_main.textSolution

class MainActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Application.initResources(resources)
    setContentView(R.layout.activity_main)
    SolvingScreen(equationEditText, textSolution, textError, keyboard)
  }
}