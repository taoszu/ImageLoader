package com.taoszu.imageloader.glide

/**
 * Copyright (C) 2018 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class RoundedCornersTransformation @JvmOverloads constructor(private val radius: Float, private val margin: Float, private val cornerType: CornerType = CornerType.ALL) : BitmapTransformation() {

  private val diameter = radius.toInt() * 2

  enum class CornerType {
    ALL,
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
    TOP, BOTTOM, LEFT, RIGHT,
    OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
    DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
  }

  override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
    val width = toTransform.width
    val height = toTransform.height

    val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setHasAlpha(true)

    val canvas = Canvas(bitmap)
    val paint = Paint()
    paint.isAntiAlias = true
    paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
    return bitmap
  }

  private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
    val right = width - margin
    val bottom = height - margin

    when (cornerType) {
      RoundedCornersTransformation.CornerType.ALL -> canvas.drawRoundRect(RectF(margin, margin, right, bottom), radius, radius, paint)
      RoundedCornersTransformation.CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.TOP -> drawTopRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.LEFT -> drawLeftRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.RIGHT -> drawRightRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom)
      RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom)
    }
  }

  private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, (margin + diameter), (margin + diameter)), radius,
            radius, paint)
    canvas.drawRect(RectF(margin, (margin + radius), (margin + radius), bottom), paint)
    canvas.drawRect(RectF((margin + radius), margin, right, bottom), paint)
  }

  private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(right - diameter, margin, right, (margin + diameter)), radius,
            radius, paint)
    canvas.drawRect(RectF(margin, margin, right - radius, bottom), paint)
    canvas.drawRect(RectF(right - radius, (margin + radius), right, bottom), paint)
  }

  private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, bottom - diameter, (margin + diameter), bottom), radius,
            radius, paint)
    canvas.drawRect(RectF(margin, margin, (margin + diameter), bottom - radius), paint)
    canvas.drawRect(RectF((margin + radius), margin, right, bottom), paint)
  }

  private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(right - diameter, bottom - diameter, right, bottom), radius,
            radius, paint)
    canvas.drawRect(RectF(margin, margin, right - radius, bottom), paint)
    canvas.drawRect(RectF(right - radius, margin, right, bottom - radius), paint)
  }

  private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, right, (margin + diameter)), radius, radius,
            paint)
    canvas.drawRect(RectF(margin, (margin + radius), right, bottom), paint)
  }

  private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, bottom - diameter, right, bottom), radius, radius,
            paint)
    canvas.drawRect(RectF(margin, margin, right, bottom - radius), paint)
  }

  private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, (margin + diameter), bottom), radius, radius,
            paint)
    canvas.drawRect(RectF((margin + radius), margin, right, bottom), paint)
  }

  private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(right - diameter, margin, right, bottom), radius, radius, paint)
    canvas.drawRect(RectF(margin, margin, right - radius, bottom), paint)
  }

  private fun drawOtherTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, bottom - diameter, right, bottom), radius, radius,
            paint)
    canvas.drawRoundRect(RectF(right - diameter, margin, right, bottom), radius, radius, paint)
    canvas.drawRect(RectF(margin, margin, right - radius, bottom - radius), paint)
  }

  private fun drawOtherTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, (margin + diameter), bottom), radius, radius,
            paint)
    canvas.drawRoundRect(RectF(margin, bottom - diameter, right, bottom), radius, radius,
            paint)
    canvas.drawRect(RectF((margin + radius), margin, right, bottom - radius), paint)
  }

  private fun drawOtherBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, right, (margin + diameter)), radius, radius,
            paint)
    canvas.drawRoundRect(RectF(right - diameter, margin, right, bottom), radius, radius, paint)
    canvas.drawRect(RectF(margin, (margin + radius), right - radius, bottom), paint)
  }

  private fun drawOtherBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float,
                                            bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, right, (margin + diameter)), radius, radius,
            paint)
    canvas.drawRoundRect(RectF(margin, margin, (margin + diameter), bottom), radius, radius,
            paint)
    canvas.drawRect(RectF((margin + radius), (margin + radius), right, bottom), paint)
  }

  private fun drawDiagonalFromTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float,
                                               bottom: Float) {
    canvas.drawRoundRect(RectF(margin, margin, (margin + diameter), (margin + diameter)), radius,
            radius, paint)
    canvas.drawRoundRect(RectF(right - diameter, bottom - diameter, right, bottom), radius,
            radius, paint)
    canvas.drawRect(RectF(margin, (margin + radius), right - diameter, bottom), paint)
    canvas.drawRect(RectF((margin + diameter), margin, right, bottom - radius), paint)
  }

  private fun drawDiagonalFromTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float,
                                                bottom: Float) {
    canvas.drawRoundRect(RectF(right - diameter, margin, right, (margin + diameter)), radius,
            radius, paint)
    canvas.drawRoundRect(RectF(margin, bottom - diameter, (margin + diameter), bottom), radius,
            radius, paint)
    canvas.drawRect(RectF(margin, margin, right - radius, bottom - radius), paint)
    canvas.drawRect(RectF((margin + radius), (margin + radius), right, bottom), paint)
  }

  override fun toString(): String {
    return ("RoundedTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
            + diameter + ", cornerType=" + cornerType.name + ")")
  }

  override fun equals(o: Any?): Boolean {
    return o is RoundedCornersTransformation &&
            o.radius == radius &&
            o.diameter == diameter &&
            o.margin == margin &&
            o.cornerType == cornerType
  }

  override fun hashCode(): Int {
    return ID.hashCode() + radius.toInt() * 10000 + diameter * 1000 + margin.toInt() * 100 + cornerType.ordinal * 10
  }

  override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    messageDigest.update((ID + radius + diameter + margin + cornerType).toByteArray(CHARSET))
  }

  companion object {

    private val VERSION = 1
    private val ID = "com.taoszu.imageloader.glide.RoundedCornersTransformation.$VERSION"
  }
}