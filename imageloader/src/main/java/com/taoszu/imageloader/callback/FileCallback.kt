package com.taoszu.imageloader.callback

import java.io.File

interface FileCallback {

  fun onFailed()

  fun onSuccess(file: File)

}