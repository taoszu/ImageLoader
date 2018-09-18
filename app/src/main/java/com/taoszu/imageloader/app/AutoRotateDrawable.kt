package com.taoszu.imageloader.app

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.SystemClock

class AutoRotateDrawable(drawable:Drawable) : Drawable(), Runnable {

  val drawable = drawable as BitmapDrawable

  private val DEGREES_IN_FULL_ROTATION = 360
  private val FRAME_INTERVAL_MS = 20

  // Specified duration in milliseconds for one complete rotation.
  private val mInterval: Int = 1000
  // Specified rotation direction
  private var mClockwise: Boolean = false

  internal var mRotationAngle = 0f

  private var mIsScheduled = false

  override fun draw(canvas: Canvas?) {
    canvas?.let {
      val saveCount = it.save()

      val bounds = bounds
      val width = bounds.right - bounds.left
      val height = bounds.bottom - bounds.top

      var angle = mRotationAngle
      if (!mClockwise) {
        angle = DEGREES_IN_FULL_ROTATION - mRotationAngle
      }

      it.rotate(angle, (bounds.left + width / 2).toFloat(), (bounds.top + height / 2).toFloat())
      //canvas.drawColor(Color.RED)

      canvas.drawBitmap(drawable.bitmap, bounds, bounds, Paint())

      it.restoreToCount(saveCount)

      scheduleNextFrame()
    }

  }

  override fun setAlpha(alpha: Int) {
    drawable.alpha = alpha
  }


  private fun scheduleNextFrame() {
    if (!mIsScheduled) {
      mIsScheduled = true
      scheduleSelf(this, SystemClock.uptimeMillis() + FRAME_INTERVAL_MS)
    }
  }

  override fun run() {
    mIsScheduled = false
    mRotationAngle += getIncrement().toFloat()
    invalidateSelf()
  }

  override fun getOpacity(): Int {
    return drawable.opacity
  }


  override fun setColorFilter(colorFilter: ColorFilter?) {
    drawable.colorFilter = colorFilter
  }

  private fun getIncrement(): Int {
    return (FRAME_INTERVAL_MS.toFloat() / mInterval * DEGREES_IN_FULL_ROTATION).toInt()
  }
}