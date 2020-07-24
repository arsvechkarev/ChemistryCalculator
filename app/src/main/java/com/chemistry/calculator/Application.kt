package com.chemistry.calculator

import timber.log.Timber
import android.app.Application as AndroidApplication

class Application : AndroidApplication() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}