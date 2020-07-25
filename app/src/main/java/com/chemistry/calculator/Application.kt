package com.chemistry.calculator

import android.content.res.Resources
import timber.log.Timber
import android.app.Application as AndroidApplication

class Application : AndroidApplication() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
  
  companion object {
    
    var density = -1f
      private set
    
    fun initResources(resources: Resources) {
      density = resources.displayMetrics.density
    }
  }
}