package com.taoszu.imageloader.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.taoszu.imageloader.MemoryTools

@GlideModule
class CustomGlideModule : AppGlideModule() {

  override fun isManifestParsingEnabled(): Boolean {
    return false
  }

  override fun applyOptions(context: Context, builder: GlideBuilder) {
    super.applyOptions(context, builder)

    val memoryCacheSize = MemoryTools.getDefaultMaxCacheSize(context)
    builder.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
  }

}