package com.taoszu.imageloader

class LoadOptions private constructor(builder : Builder) {

  var placeHolderRes: Int = 0
  var failureRes: Int = 0
  var isWrapContent:Boolean = false
  var roundParams:RoundParams? = null
  var asCircle = false

  init {
    placeHolderRes = builder.placeHolderRes
    isWrapContent = builder.isWrapContent
    failureRes = builder.failureRes
    roundParams = builder.roundParams
    asCircle = builder.asCircle
  }

  class Builder {
    internal var placeHolderRes: Int = 0
    internal var isWrapContent:Boolean = false
    internal var failureRes: Int = 0
    internal var roundParams:RoundParams? = null
    internal var asCircle = false

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

    fun roundParams(roundParams:RoundParams):Builder {
      this.roundParams = roundParams
      return this
    }

    fun asCircle():Builder {
      asCircle = true
      return this
    }

    fun build(): LoadOptions {
      return LoadOptions(this)
    }
  }

}