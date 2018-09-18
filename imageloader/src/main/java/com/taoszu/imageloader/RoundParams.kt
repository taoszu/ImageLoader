package com.taoszu.imageloader

import android.content.Context

class RoundParams {

  var radius:Float = 0f

  /**
   * @param radius corner radius in pixels
   */
  fun setRadius(radius:Float):RoundParams {
    this.radius = radius
    return this
  }

  /**
   * @param radius corner radius in dp
   */
  fun setRadiusDp(context: Context, radius: Float):RoundParams {
    return setRadius(ImageTools.dp2px(context, radius))
  }


}