package com.taoszu.imageloader.glide

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import android.graphics.Bitmap


class CircleTransformation : BitmapTransformation() {

  private val VERSION = 1
  private val ID = "jp.wasabeef.glide.transformations.CropCircleTransformation.$VERSION"

  override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
    return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight)
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

}