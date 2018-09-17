package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView

object ImageLoaderManager : ImageLoaderFrame() {

  private var imageLoader: ImageLoaderFrame? = null

  fun injectLoader(imageLoader: ImageLoaderFrame):ImageLoaderManager {
    this.imageLoader = imageLoader
    return this
  }

  override fun init(context: Context) {
    imageLoader?.init(context)
  }

  override fun init(context: Context, frameConfig: FrameConfig) {
    imageLoader?.init(context, frameConfig)
  }

  override fun loadUri(view: ImageView, uriString: String, loaderOptions: LoadOptions) {
    imageLoader?.loadUri(view, uriString, loaderOptions)
  }

  override fun loadRes(view: ImageView, resId: Int, loaderOptions: LoadOptions) {
    imageLoader?.loadRes(view, resId, loaderOptions)
  }

  override fun getBitmap(context: Context, uriString: String): Bitmap? {
    return imageLoader?.getBitmap(context, uriString)
  }

  override fun clearDiskCache(context: Context) {
    imageLoader?.clearDiskCache(context)
  }

  override fun clearMemoryCache(context: Context) {
    imageLoader?.clearMemoryCache(context)
  }


}