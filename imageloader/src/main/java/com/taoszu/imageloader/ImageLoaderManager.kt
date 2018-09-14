package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.view.View

object ImageLoaderManager : ImageLoaderFrame{

  private var imageLoader: ImageLoaderFrame? = null

  fun injectLoader(imageLoader: ImageLoaderFrame) {
    this.imageLoader = imageLoader
  }

  override fun loadUri(view: View, uriString: String, loaderConfig: LoadConfig) {
    imageLoader?.loadUri(view, uriString, loaderConfig)
  }

  override fun loadRes(view: View, resId: Int) {
    imageLoader?.loadRes(view, resId)
  }

  override fun getBitmap(context: Context, uriString: String): Bitmap? {
    return imageLoader?.getBitmap(context, uriString)
  }

}