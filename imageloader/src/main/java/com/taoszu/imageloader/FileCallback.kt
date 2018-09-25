package com.taoszu.imageloader

import java.io.File

interface FileCallback {

  fun onFailed()

  fun onSuccess(file: File)

}