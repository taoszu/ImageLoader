package com.taoszu.imageloader.config

import android.content.Context
import com.taoszu.imageloader.tool.ImageTools
import java.lang.IllegalArgumentException

class LoadOptions private constructor(builder : Builder) {

  var progressRes:Int = 0
  var placeHolderRes: Int = 0
  var failureRes: Int = 0
  var roundParams: RoundParams? = null
  var imageSize: ImageSize? = null

  var asCircle = false

  init {
    progressRes = builder.progressRes
    placeHolderRes = builder.placeHolderRes
    failureRes = builder.failureRes
    roundParams = builder.roundParams
    asCircle = builder.asCircle
    imageSize = builder.imageSize
  }

  class Builder {
    internal var progressRes: Int = 0
    internal var placeHolderRes: Int = 0
    internal var failureRes: Int = 0
    internal var roundParams: RoundParams? = null
    internal var asCircle = false
    internal var imageSize: ImageSize? = null

    fun progress(progressRes: Int): Builder {
      this.progressRes = progressRes
      return this
    }

    fun placeHolder(placeHolderRes: Int): Builder {
      this.placeHolderRes = placeHolderRes
      return this
    }

    fun failure(failureRes: Int): Builder {
      this.failureRes = failureRes
      return this
    }

    fun roundParams(roundParams: RoundParams): Builder {
      this.roundParams = roundParams
      return this
    }

    fun asCircle(): Builder {
      asCircle = true
      return this
    }

    fun sizeDp(context: Context, width:Float, height:Float): Builder {
      return size(ImageTools.dp2px(context, width).toInt(), ImageTools.dp2px(context, height).toInt())
    }

    fun size(width:Int, height:Int): Builder {
      if (width <= 0 || height <= 0) {
        throw IllegalArgumentException("width and height must not small than 0")
      }
      imageSize = ImageSize(width, height)
      return this
    }

    fun build(): LoadOptions {
      return LoadOptions(this)
    }
  }

}