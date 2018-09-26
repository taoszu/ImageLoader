package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView

abstract class ImageLoaderFrame {


  private val defaultOptions = LoadOptions.Builder().build()

  private fun clearTotalCache(context: Context) {
    clearDiskCache(context)
    clearMemoryCache(context)
  }

  fun loadUri(imageView:ImageView, uriString:String?) {
    loadUri(imageView, uriString, defaultOptions)
  }

  fun loadUri(imageView:ImageView, uriString:String?, imageInfoCallback: ImageInfoCallback?) {
    loadUri(imageView, uriString, defaultOptions, imageInfoCallback)
  }

  fun loadRes(imageView:ImageView, resId:Int) {
    loadRes(imageView, resId, defaultOptions)
  }

  abstract fun init(context: Context, frameConfig: FrameConfig)

  abstract fun init(context: Context)

  abstract fun loadUri(imageView:ImageView, uriString:String?, loaderOptions: LoadOptions)

  abstract fun loadUri(imageView:ImageView, uriString:String?, loaderOptions: LoadOptions, imageInfoCallback: ImageInfoCallback?)

  abstract fun loadRes(imageView:ImageView, resId:Int, loaderOptions: LoadOptions)

  abstract fun getBitmap(context: Context, uriString: String?):Bitmap?

  abstract fun requestFile(context: Context, uriString: String?, fileCallback: FileCallback)

  abstract fun clearMemoryCache(context: Context)

  abstract fun clearDiskCache(context: Context)

  abstract fun getDiskCache(context: Context):Long
}