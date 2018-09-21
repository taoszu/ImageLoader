package com.taoszu.imageloader

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.View

class AutoRotateDrawable(drawable:Drawable, rootView: View) : Drawable(), Runnable {

  val drawable = drawable as BitmapDrawable

  private val DEGREES_IN_FULL_ROTATION = 360
  private val FRAME_INTERVAL_MS = 20

  // Specified duration in milliseconds for one complete rotation.
  private val mInterval: Int = 1000
  // Specified rotation direction
  private var mClockwise: Boolean = false

  private var mRotationAngle = 0f

  private var mIsScheduled = false

  private val paint = Paint()

  private var scaleBounds: Rect? = null

  private val context = rootView.context.applicationContext

  private val rootView = rootView

  init {
    paint.isAntiAlias = true
  }

  override fun draw(canvas: Canvas?) {
    canvas?.let {
      val saveCount = it.save()

      if (scaleBounds == null) {
        val left = bounds.left
        val top  = bounds.top
        val right = ImageTools.dp2px(context, drawable.bitmap.width.toFloat())
        val bottom = ImageTools.dp2px(context, drawable.bitmap.height.toFloat())
        scaleBounds = Rect(left, top, right.toInt(), bottom.toInt())
      }

      val bounds = scaleBounds!!
      var angle = mRotationAngle
      if (!mClockwise) {
        angle = DEGREES_IN_FULL_ROTATION - mRotationAngle
      }

      val rotateX = (bounds.left + bounds.right) /2
      val rotateY = (bounds.top + bounds.bottom) /2
      val translateX = rootView.pivotX - bounds.width()/2
      val translateY = rootView.pivotY - bounds.height()/2

      it.translate(translateX, translateY)
      it.rotate(angle, rotateX.toFloat(), rotateY.toFloat())

      it.drawBitmap(drawable.bitmap, bounds, bounds ,paint)

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