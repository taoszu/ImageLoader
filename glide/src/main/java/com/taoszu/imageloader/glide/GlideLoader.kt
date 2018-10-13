package com.taoszu.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.taoszu.imageloader.*
import com.taoszu.imageloader.callback.FileCallback
import com.taoszu.imageloader.callback.ImageInfoCallback
import com.taoszu.imageloader.config.FrameConfig
import com.taoszu.imageloader.config.ImageSize
import com.taoszu.imageloader.config.LoadOptions
import com.taoszu.imageloader.tool.ImageTools
import java.io.File

class GlideLoader : ImageLoaderFrame() {

  override fun init(context: Context) {
    // 需要自己定义GlideModule
  }

  override fun init(context: Context, frameConfig: FrameConfig) {
    // 需要自己定义GlideModule
  }

  override fun requestFile(context: Context, uriString: String?, fileCallback: FileCallback) {
    Glide.with(context)
            .downloadOnly()
            .load(uriString)
            .into(object : SimpleTarget<File>() {
              override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                fileCallback.onSuccess(resource)
              }

              override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                fileCallback.onFailed()
              }
            })
  }

  @WorkerThread
  override fun getBitmap(context: Context, uriString: String?): Bitmap? {
    return try {
      Glide.with(context)
              .asBitmap()
              .load(uriString)
              .submit()
              .get()
    } catch (e: Exception) {
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
            .into(object : CustomViewTarget<ImageView, Drawable>(glideView) {
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


  @MainThread
  override fun clearMemoryCache(context: Context) {
    Glide.get(context).clearMemory()
  }

  @WorkerThread
  override fun clearDiskCache(context: Context) {
    Glide.get(context).clearDiskCache()
  }

  @WorkerThread
  override fun getDiskCache(context: Context): Long {
    val fileDir = Glide.getPhotoCacheDir(context)
    fileDir?.let {
      val fileList = it.listFiles()
      var totalSize = 0L
      for (i in 0 until fileList.size) {
        totalSize += fileList[i].length()
      }
      return totalSize
    }
    return 0
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