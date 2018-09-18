package com.taoszu.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.WorkerThread
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.taoszu.imageloader.FrameConfig
import com.taoszu.imageloader.ImageLoaderFrame
import com.taoszu.imageloader.ImageTools
import com.taoszu.imageloader.LoadOptions

class GlideLoader :ImageLoaderFrame() {

  override fun init(context: Context) {
    // 需要自己定义GlideModule
  }

  override fun init(context: Context, frameConfig: FrameConfig) {
    // 需要自己定义GlideModule
  }

  @WorkerThread
  override fun getBitmap(context: Context, uriString: String?):Bitmap? {
    return Glide.with(context)
            .asBitmap()
            .load(uriString)
            .submit()
            .get()
  }

  override fun loadRes(glideView: ImageView, resId: Int, loaderOptions: LoadOptions) {
    val requestOptions = buildOptions(loaderOptions)
    Glide.with(glideView.context)
            .load(resId)
            .apply(requestOptions)
            .into(glideView)
  }

  override fun loadUri(glideView: ImageView, uriString: String?, loaderOptions: LoadOptions) {
    if (loaderOptions.isWrapContent) {
      wrapContentRequest(glideView, uriString, loaderOptions)
    } else {

      val requestOptions = buildOptions(loaderOptions)
      requestOptions.fitCenter()

      Glide.with(glideView.context)
              .load(uriString)
              .apply(requestOptions)
              .into(object:CustomViewTarget<ImageView, Drawable>(glideView) {
                override fun onResourceCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                  view.setImageDrawable(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                  errorDrawable?.let {
                    view.setImageDrawable(it)
                  }
                }

                override fun onResourceLoading(placeholder: Drawable?) {
                  super.onResourceLoading(placeholder)
                  placeholder?.let {
                    view.setImageDrawable(it)
                  }
                }

              })
    }
  }


  override fun clearMemoryCache(context: Context) {

  }

  override fun clearDiskCache(context: Context) {
  }

  private fun wrapContentRequest(glideView: ImageView, uriString: String?, loaderOptions: LoadOptions) {
    val requestOptions = buildOptions(loaderOptions)

    val customViewTarget = object: CustomViewTarget<ImageView, Drawable>(glideView) {
      override fun onResourceCleared(placeholder: Drawable?) {}

      override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        val bitmap = (resource as BitmapDrawable).bitmap
        val width = bitmap.width
        val height = bitmap.height

        ImageTools.resizeView(view, width, height)
        view.setImageDrawable(resource)
      }

      override fun onLoadFailed(errorDrawable: Drawable?) {
        errorDrawable?.let {
          view.setImageDrawable(it)
        }
      }

      override fun onResourceLoading(placeholder: Drawable?) {
        super.onResourceLoading(placeholder)
        placeholder?.let {
          view.setImageDrawable(it)
        }
      }
    }

    Glide.with(glideView.context)
            .load(uriString)
            .apply(requestOptions)
            .into(customViewTarget)
  }

  private fun buildOptions(loadOptions: LoadOptions):RequestOptions {
    val requestOptions = RequestOptions()
    if (loadOptions.failureRes != 0) {
      requestOptions.error(loadOptions.failureRes)
    }
    if (loadOptions.placeHolderRes != 0) {
      requestOptions.placeholder(loadOptions.placeHolderRes)
    }

    if (loadOptions.asCircle) {
      requestOptions.transform(CircleTransformation())
    }

    loadOptions.roundParams?.let {
      requestOptions.transform(RoundedCornersTransformation(it.radius, 0f))
    }

    return requestOptions
  }

}