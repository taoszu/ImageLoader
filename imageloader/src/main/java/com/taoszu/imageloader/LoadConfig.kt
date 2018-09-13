package com.taoszu.imageloader

class LoadConfig private constructor(builder : Builder) {

  var placeHolderRes: Int = 0

  init {
    placeHolderRes = builder.placeHolderRes
  }

  class Builder {
    internal var placeHolderRes: Int = 0

    fun placeHolder(placeHolderRes: Int):Builder {
      this.placeHolderRes = placeHolderRes
      return this
    }

    fun build(): LoadConfig {
      return LoadConfig(this)
    }
  }

}