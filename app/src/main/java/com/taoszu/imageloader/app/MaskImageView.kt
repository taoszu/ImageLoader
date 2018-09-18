package com.taoszu.imageloader.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView

class MaskImageView : ImageView {

  private var isRoundsCircle = false
  private var maskColor = 0x4c000000
    set(value) {
      field  = value
      invalidate()
    }


  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    canvas?.let {
      drawMask(canvas)
    }
  }

  private fun drawMask(canvas: Canvas) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = maskColor
    val rect = RectF(0.0f, 0.0f, canvas.width.toFloat(), canvas.height.toFloat())
    if (isRoundsCircle) {
      canvas.drawRect(rect, paint)
    } else {
      val radius = Math.min(canvas.width.toFloat(), canvas.height.toFloat()) / 2
      val path = Path()
      path.addCircle(rect.centerX(), rect.centerY(), radius, Path.Direction.CW)
      canvas.drawPath(path, paint)
    }
  }



}