package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.view.View

interface ImageLoaderFrame {

  fun init(context: Context, frameConfig: FrameConfig)

  fun init(context: Context)

  fun loadUri(view:View, uriString:String, loaderConfig: LoadConfig)

  fun loadRes(view:View, resId:Int, loaderConfig: LoadConfig)

  fun getBitmap(context: Context, uriString: String):Bitmap?

  fun clearTotalCache(context: Context)

  fun clearMemoryCache(context: Context)

  fun clearDiskCache(context: Context)
}