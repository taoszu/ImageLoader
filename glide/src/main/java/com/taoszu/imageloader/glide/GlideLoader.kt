package com.taoszu.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.WorkerThread
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.taoszu.imageloader.FrameConfig
import com.taoszu.imageloader.ImageLoaderFrame
import com.taoszu.imageloader.ImageTools
import com.taoszu.imageloader.LoadConfig

class GlideLoader :ImageLoaderFrame {


  override fun init(context: Context) {
    // 需要自己定义GlideModule
  }

  override fun init(context: Context, frameConfig: FrameConfig) {
    // 需要自己定义GlideModule
  }

  @WorkerThread
  override fun getBitmap(context: Context, uriString: String):Bitmap? {
    return Glide.with(context)
            .asBitmap()
            .load(uriString)
            .submit()
            .get()
  }

  override fun loadRes(view: View, resId: Int, loaderConfig: LoadConfig) {
    val glideView = transformGlideView(view)
    val requestOptions = buildOptions(loaderConfig)

    Glide.with(view.context)
            .load(resId)
            .apply(requestOptions)
            .into(glideView)
  }

  override fun loadUri(view: View, uriString: String, loaderConfig: LoadConfig) {
    val glideView = view as ImageView
    if (loaderConfig.isWrapContent) {
      wrapContentRequest(glideView, uriString, loaderConfig)
    } else {
      val requestOptions = buildOptions(loaderConfig)
      Glide.with(view.context)
              .load(uriString)
              .apply(requestOptions)
              .into(glideView)
    }
  }

  override fun clearTotalCache(context: Context) {
  }

  override fun clearMemoryCache(context: Context) {

  }

  override fun clearDiskCache(context: Context) {
  }

  private fun wrapContentRequest(glideView: ImageView, uriString: String, loaderConfig: LoadConfig) {
    val requestOptions = buildOptions(loaderConfig)

    val customViewTarget = object: CustomViewTarget<ImageView, Drawable>(glideView) {
      override fun onResourceCleared(placeholder: Drawable?) {}

      override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        val bitmap = (resource as BitmapDrawable).bitmap
        val width = bitmap.width
        val height = bitmap.height

        ImageTools.resizeView(view, width, height)
        view.background = resource
      }

      override fun onLoadFailed(errorDrawable: Drawable?) {
        view.setBackgroundResource(loaderConfig.failureRes)
      }

      override fun onResourceLoading(placeholder: Drawable?) {
        super.onResourceLoading(placeholder)
        view.setBackgroundResource(loaderConfig.placeHolderRes)
      }
    }

    Glide.with(glideView.context)
            .load(uriString)
            .apply(requestOptions)
            .into(customViewTarget)
  }

  private fun buildOptions(loadConfig: LoadConfig):RequestOptions {
    val requestOptions = RequestOptions()
    if (loadConfig.failureRes != 0) {
      requestOptions.error(loadConfig.failureRes)
    }
    if (loadConfig.placeHolderRes != 0) {
      requestOptions.placeholder(loadConfig.placeHolderRes)
    }

    if (loadConfig.asCircle) {
      requestOptions.transform(CircleTransformation())
    }

    return requestOptions
  }

  private fun transformGlideView(view:View): ImageView {
    return view as ImageView
  }

}