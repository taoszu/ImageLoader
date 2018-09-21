package com.taoszu.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.WorkerThread
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.taoszu.imageloader.*

class GlideLoader : ImageLoaderFrame() {

  override fun init(context: Context) {
    // 需要自己定义GlideModule
  }

  override fun init(context: Context, frameConfig: FrameConfig) {
    // 需要自己定义GlideModule
  }

  @WorkerThread
  override fun getBitmap(context: Context, uriString: String?): Bitmap? {
    return try {
      Glide.with(context)
              .asBitmap()
              .load(uriString)
              .submit()
              .get()
    } catch (e:Exception) {
      null
    }
  }

  override fun loadRes(glideView: ImageView, resId: Int, loaderOptions: LoadOptions) {
    val requestOptions = buildOptions(loaderOptions)
    Glide.with(glideView.context)
            .load(resId)
            .apply(requestOptions)
            .into(glideView)
  }

  override fun loadUri(glideView: ImageView, uriString: String?, loaderOptions: LoadOptions) {
    loadUri(glideView, uriString, loaderOptions, null)
  }

  override fun loadUri(glideView: ImageView, uriString: String?, loaderOptions: LoadOptions, imageInfoCallback: ImageInfoCallback?) {
    val requestOptions = buildOptions(loaderOptions)
    loaderOptions.imageSize?.let {
      requestOptions.override(it.width, it.height)
    }

    if (loaderOptions.progressRes != 0) {
      if (glideView.layoutParams == null || (glideView.layoutParams.height < 0 || glideView.layoutParams.width < 0)) {
        ImageTools.resizeView(glideView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      }
    }

    Glide.with(glideView.context)
            .load(uriString)
            .apply(requestOptions)
            .into(object: CustomViewTarget<ImageView, Drawable>(glideView) {
              override fun onLoadFailed(errorDrawable: Drawable?) {
                errorDrawable?.let {
                  view.setImageDrawable(errorDrawable)
                }
              }

              override fun onResourceLoading(placeholder: Drawable?) {
                super.onResourceLoading(placeholder)

                if (loaderOptions.progressRes != 0) {
                  glideView.setImageDrawable(AutoRotateDrawable(glideView.resources.getDrawable(loaderOptions.progressRes), glideView))
                } else if (placeholder != null) {
                  view.setImageDrawable(placeholder)
                }
              }

              override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.setImageDrawable(resource)

                val width = resource.intrinsicWidth
                val height = resource.intrinsicHeight
                if (width > 0 && height > 0) {
                  imageInfoCallback?.onSuccess(ImageSize(width, height))
                } else {
                  imageInfoCallback?.onFailed()
                }

                if (loaderOptions.progressRes != 0) {
                  ImageTools.resizeView(glideView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
              }

              override fun onResourceCleared(placeholder: Drawable?) {}

            })
  }


  override fun clearMemoryCache(context: Context) {

  }

  override fun clearDiskCache(context: Context) {
  }

  private fun buildOptions(loadOptions: LoadOptions): RequestOptions {
    val requestOptions = RequestOptions()
    if (loadOptions.failureRes != 0) {
      requestOptions.error(loadOptions.failureRes)
    }

    if (loadOptions.placeHolderRes != 0 && loadOptions.progressRes == 0) {
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