package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView

object ImageLoaderManager : ImageLoaderFrame {

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

  override fun loadUri(view: ImageView, uriString: String, loaderConfig: LoadConfig) {
    imageLoader?.loadUri(view, uriString, loaderConfig)
  }

  override fun loadRes(view: ImageView, resId: Int, loaderConfig: LoadConfig) {
    imageLoader?.loadRes(view, resId, loaderConfig)
  }

  override fun getBitmap(context: Context, uriString: String): Bitmap? {
    return imageLoader?.getBitmap(context, uriString)
  }

  override fun clearDiskCache(context: Context) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun clearMemoryCache(context: Context) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun clearTotalCache(context: Context) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}