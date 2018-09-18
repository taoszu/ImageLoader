package com.taoszu.imageloader

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.util.TypedValue



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

  fun dp2px(context: Context, dpValue:Float):Float {
   return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics)
  }

}