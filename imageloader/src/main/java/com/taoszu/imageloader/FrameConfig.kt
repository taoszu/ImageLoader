package com.taoszu.imageloader

class FrameConfig private constructor(builder : Builder) {

  var memoryCacheSize: Long = 128 * 1024 * 1024L

  init {
    memoryCacheSize = builder.memoryCacheSize

  }

  class Builder {
    internal var memoryCacheSize: Long = 128 * 1024 * 1024L

    fun memoryCacheSize(memoryCacheSize:Long):Builder {
      this.memoryCacheSize = memoryCacheSize
      return this
    }

    fun build(): FrameConfig {
      return FrameConfig(this)
    }
  }

}