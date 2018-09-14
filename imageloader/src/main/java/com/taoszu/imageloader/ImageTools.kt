package com.taoszu.imageloader

import android.view.View
import android.view.ViewGroup

object ImageTools {

  fun resizeView(view: View, width: Int, height: Int) {
    var layoutParams = view.layoutParams
    if (layoutParams == null) {
      layoutParams = ViewGroup.LayoutParams(width, height)
      view.layoutParams = layoutParams
    } else {
      view.layoutParams.width = width
      view.layoutParams.height = height
      view.requestLayout()
    }
  }

  fun getMaxWidth(view: View):Int {
    return view.resources.displayMetrics.widthPixels
  }

  fun getMaxHeight(view: View):Int {
    return view.resources.displayMetrics.heightPixels
  }

}