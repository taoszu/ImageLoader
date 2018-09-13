package com.taoszu.imageloader

import android.view.View

object ImageLoaderManager {

  private var imageLoader: ImageLoaderStrategy? = null

  fun injectLoader(imageLoader: ImageLoaderStrategy) {
    this.imageLoader = imageLoader
  }


  fun load(view: View, uriString: String, loaderConfig: LoadConfig) {
    imageLoader?.load(view, uriString, loaderConfig)
  }

}