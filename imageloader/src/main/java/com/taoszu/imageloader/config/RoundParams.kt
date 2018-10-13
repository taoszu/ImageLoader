package com.taoszu.imageloader.config

import android.content.Context
import com.taoszu.imageloader.tool.ImageTools

class RoundParams {

  var radius:Float = 0f

  /**
   * @param radius corner radius in pixels
   */
  fun setRadius(radius:Float): RoundParams {
    this.radius = radius
    return this
  }

  /**
   * @param radius corner radius in dp
   */
  fun setRadiusDp(context: Context, radius: Float): RoundParams {
    return setRadius(ImageTools.dp2px(context, radius))
  }


}