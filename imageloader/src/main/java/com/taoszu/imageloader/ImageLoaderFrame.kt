package com.taoszu.imageloader

import android.view.View

interface ImageLoaderFrame {

  fun load(view:View, uriString:String, loaderConfig: LoadConfig)



}