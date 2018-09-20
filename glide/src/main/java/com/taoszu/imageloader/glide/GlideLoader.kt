package com.taoszu.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.WorkerThread
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    loadUri(glideView, uriString, loaderOptions, null)
  }

  override fun loadUri(glideView: ImageView, uriString: String?, loaderOptions: LoadOptions, imageInfoCallback: ImageInfoCallback?) {
    val requestOptions = buildOptions(loaderOptions)
    loaderOptions.imageSize?.let {
      requestOptions.override(it.width, it.height)
    }
    Glide.with(glideView.context)
            .load(uriString)
            .apply(requestOptions)
            .into(object : DrawableImageViewTarget(glideView) {
              override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                super.onResourceReady(resource, transition)

                val bitmap = (resource as BitmapDrawable).bitmap
                imageInfoCallback?.onSuccess(ImageSize(bitmap.width, bitmap.height))
              }

              override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)

                imageInfoCallback?.onFailed()
              }
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