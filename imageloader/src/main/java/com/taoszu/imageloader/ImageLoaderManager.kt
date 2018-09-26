package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import java.io.File

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

  override fun loadUri(imageView: ImageView, uriString: String?, loaderOptions: LoadOptions, imageInfoCallback: ImageInfoCallback?) {
    imageLoader?.loadUri(imageView, uriString, loaderOptions, imageInfoCallback)
  }

  override fun loadUri(view: ImageView, uriString: String?, loaderOptions: LoadOptions) {
    imageLoader?.loadUri(view, uriString, loaderOptions)
  }

  override fun loadRes(view: ImageView, resId: Int, loaderOptions: LoadOptions) {
    imageLoader?.loadRes(view, resId, loaderOptions)
  }

  override fun getBitmap(context: Context, uriString: String?): Bitmap? {
    return imageLoader?.getBitmap(context, uriString)
  }

  override fun requestFile(context: Context, uriString: String?, fileCallback: FileCallback) {
    imageLoader?.requestFile(context, uriString, fileCallback)
  }

  override fun clearDiskCache(context: Context) {
    imageLoader?.clearDiskCache(context)
  }

  override fun clearMemoryCache(context: Context) {
    imageLoader?.clearMemoryCache(context)
  }

  override fun getDiskCache(context: Context): Long {
    return imageLoader?.getDiskCache(context) ?: 0
  }

}