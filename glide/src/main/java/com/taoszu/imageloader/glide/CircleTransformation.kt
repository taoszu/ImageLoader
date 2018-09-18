package com.taoszu.imageloader.glide

import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class CircleTransformation : BitmapTransformation() {

  private val VERSION = 1
  private val ID = "com.taoszu.imageloader.glide.CircleTransformation.$VERSION"

  override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
    return circleCrop(pool, toTransform, outWidth, outHeight)
  }

  override fun toString(): String {
    return "CropCircleTransformation()"
  }

  override fun equals(other: Any?): Boolean {
    return other is CircleTransformation
  }

  override fun hashCode(): Int {
    return ID.hashCode()
  }

  override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    messageDigest.update(ID.toByteArray(CHARSET))
  }


  fun circleCrop(pool: BitmapPool, inBitmap: Bitmap,
                 destWidth: Int, destHeight: Int): Bitmap {
    val destMinEdge = Math.min(destWidth, destHeight)
    val radius = destMinEdge / 2f

    val srcWidth = inBitmap.width
    val srcHeight = inBitmap.height

    val scaleX = destMinEdge / srcWidth.toFloat()
    val scaleY = destMinEdge / srcHeight.toFloat()
    val maxScale = Math.max(scaleX, scaleY)

    val scaledWidth = maxScale * srcWidth
    val scaledHeight = maxScale * srcHeight
    val left = (destMinEdge - scaledWidth) / 2f
    val top = (destMinEdge - scaledHeight) / 2f

    val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

    // Alpha is required for this transformation.
    val toTransform = getAlphaSafeBitmap(pool, inBitmap)

    val outConfig = getAlphaSafeConfig(inBitmap)
    val result = pool.get(destMinEdge, destMinEdge, outConfig)
    result.setHasAlpha(true)

    val stroke = 0f
    val circlePaint = Paint()
    val canvas = Canvas(result)
    canvas.drawCircle(radius, radius, radius - stroke, circlePaint)

    val circleBitmapPaint = Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
    circleBitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(toTransform, null, destRect, circleBitmapPaint)

   /* circlePaint.color = Color.BLUE
    circlePaint.isAntiAlias = true
    circlePaint.style = Paint.Style.STROKE
    circlePaint.strokeWidth = stroke
    canvas.drawCircle(radius, radius, radius - stroke, circlePaint)
*/

    canvas.setBitmap(null)

    if (toTransform != inBitmap) {
      pool.put(toTransform)
    }

    return result
  }

  private fun getAlphaSafeBitmap(
          pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
    val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
    if (safeConfig == maybeAlphaSafe.config) {
      return maybeAlphaSafe
    }

    val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig)
    Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f /*left*/, 0f /*top*/, null /*paint*/)

    // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
    // when we're finished with it.
    return argbBitmap
  }

  private fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Avoid short circuiting the sdk check.
      if (Bitmap.Config.RGBA_F16 == inBitmap.config) { // NOPMD
        return Bitmap.Config.RGBA_F16
      }
    }

    return Bitmap.Config.ARGB_8888
  }

}