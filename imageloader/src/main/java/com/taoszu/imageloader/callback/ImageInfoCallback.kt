package com.taoszu.imageloader.callback

import com.taoszu.imageloader.config.ImageSize

interface ImageInfoCallback {

  fun onFailed()

  fun onSuccess(imageSize: ImageSize)

}