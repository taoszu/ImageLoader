package com.taoszu.imageloader

class LoadConfig private constructor(builder : Builder) {

  var placeHolderRes: Int = 0
  var failureRes: Int = 0
  var isWrapContent:Boolean = false

  init {
    placeHolderRes = builder.placeHolderRes
    isWrapContent = builder.isWrapContent
    failureRes = builder.failureRes
  }

  class Builder {
    internal var placeHolderRes: Int = 0
    internal var isWrapContent:Boolean = false
    internal var failureRes: Int = 0

    fun placeHolder(placeHolderRes: Int):Builder {
      this.placeHolderRes = placeHolderRes
      return this
    }

    fun failure(failureRes: Int):Builder {
      this.failureRes = failureRes
      return this
    }

    fun isWrapContent(isWrapContent:Boolean):Builder {
      this.isWrapContent = isWrapContent
      return this
    }

    fun build(): LoadConfig {
      return LoadConfig(this)
    }
  }

}