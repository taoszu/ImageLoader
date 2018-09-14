package com.taoszu.imageloader

import android.view.View

object ImageLoaderManager {

  private var imageLoader: ImageLoaderFrame? = null

  fun injectLoader(imageLoader: ImageLoaderFrame) {
    this.imageLoader = imageLoader
  }


  fun load(view: View, uriString: String, loaderConfig: LoadConfig) {
    imageLoader?.load(view, uriString, loaderConfig)
  }

}