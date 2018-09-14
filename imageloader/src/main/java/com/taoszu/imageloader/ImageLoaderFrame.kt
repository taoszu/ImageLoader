package com.taoszu.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.view.View

interface ImageLoaderFrame {



  fun loadUri(view:View, uriString:String, loaderConfig: LoadConfig)

  fun loadRes(view:View, resId:Int)

  fun getBitmap(context: Context, uriString: String):Bitmap?
}