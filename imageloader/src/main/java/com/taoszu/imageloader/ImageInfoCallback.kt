package com.taoszu.imageloader

interface ImageInfoCallback {

  fun onFailed()

  fun onSuccess(imageSize: ImageSize)

}