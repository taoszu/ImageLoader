package com.taoszu.imageloader.tool

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE

object MemoryTools {

  private const val KB = 1024
  private const val MB = 1024 * KB

  public fun getMaxMemorySizeFraction(context: Context, fraction:Int):Int {
    val mActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val maxMemory = Math.min(mActivityManager.memoryClass * MB, Integer.MAX_VALUE)
    return when {
      maxMemory < 32 * MB -> 4 * MB
      maxMemory < 64 * MB -> 6 * MB
      else -> maxMemory / fraction
    }
  }

  public fun getDefaultMaxCacheSize(context: Context): Int {
    return getMaxMemorySizeFraction(context, 4)
  }

}