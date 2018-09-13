package com.taoszu.imageloader

import android.view.View

interface ImageLoaderStrategy {

  fun load(view:View, uriString:String, loaderConfig: LoadConfig)



}