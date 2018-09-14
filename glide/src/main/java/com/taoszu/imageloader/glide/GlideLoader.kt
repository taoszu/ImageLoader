package com.taoszu.imageloader.glide

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.taoszu.imageloader.ImageLoaderFrame
import com.taoszu.imageloader.ImageTools
import com.taoszu.imageloader.LoadConfig

class GlideLoader : ImageLoaderFrame {

  override fun load(view: View, uriString: String, loaderConfig: LoadConfig) {
    val glideView = view as ImageView

    val requestOptions = buildOptions(loaderConfig)
    if (loaderConfig.isWrapContent) {
      val customViewTarget = object: CustomViewTarget<ImageView, Drawable>(glideView) {
        override fun onResourceCleared(placeholder: Drawable?) {}

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {

          val bitmap = (resource as BitmapDrawable).bitmap
          val width = bitmap.width
          val height = bitmap.height

          ImageTools.resizeView(view, width, height)
          view.background = resource

          Log.e("size", "$width  ---  $height")
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
          view.setBackgroundResource(loaderConfig.failureRes)
        }

        override fun onResourceLoading(placeholder: Drawable?) {
          super.onResourceLoading(placeholder)
          view.setBackgroundResource(loaderConfig.placeHolderRes)
        }
      }

      Glide.with(view.context)
              .load(uriString)
              .apply(requestOptions)
              .into(customViewTarget)

    } else {
      Glide.with(view.context)
              .load(uriString)
              .apply(requestOptions)
              .into(glideView)
    }

  }

  private fun buildOptions(loadConfig: LoadConfig):RequestOptions {
    val requestOptions = RequestOptions()
    if (loadConfig.failureRes != 0) {
      requestOptions.error(loadConfig.failureRes)
    }
    if (loadConfig.placeHolderRes != 0) {
      requestOptions.placeholder(loadConfig.placeHolderRes)
    }
    return requestOptions
  }


}