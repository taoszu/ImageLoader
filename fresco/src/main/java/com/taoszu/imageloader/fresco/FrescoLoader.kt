package com.taoszu.imageloader.fresco

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.taoszu.imageloader.ImageLoaderFrame
import com.taoszu.imageloader.LoadConfig

class FrescoLoader(context: Context) : ImageLoaderFrame {

  init {
    Fresco.initialize(context.applicationContext)
  }

  private var draweeHierarchyBuilder: GenericDraweeHierarchyBuilder? = null
  private var hierarchy: GenericDraweeHierarchy? = null

  override fun load(view: View, uriString: String, loaderConfig: LoadConfig) {
    buildDraweeView(view.resources, loaderConfig)

    val draweeView = view as SimpleDraweeView
    draweeView.hierarchy = hierarchy

    if (loaderConfig.isWrapContent) {
      loadForResize(draweeView, uriString, loaderConfig)
    } else {
      draweeView.setImageURI(uriString)
    }
  }


  private fun loadForResize(draweeView: SimpleDraweeView, uriString: String, loaderConfig: LoadConfig) {
    val controllerListener = object : BaseControllerListener<ImageInfo>() {
      override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        super.onFinalImageSet(id, imageInfo, animatable)
        imageInfo?.let {
          resizeDraweeView(draweeView, imageInfo.width, imageInfo.height)
        }
      }

      override fun onFailure(id: String?, throwable: Throwable?) {
        super.onFailure(id, throwable)

        loaderConfig.takeIf {
          it.failureRes != 0 && it.isWrapContent
        }?.let {
          val failureBitmap = BitmapFactory.decodeResource(draweeView.resources, it.failureRes)
          resizeDraweeView(draweeView, failureBitmap.width, failureBitmap.height)
          failureBitmap.recycle()
        }
      }

    }

    val controller = Fresco.newDraweeControllerBuilder()
            .setControllerListener(controllerListener)
            .setUri(uriString)
            .build()
    draweeView.controller = controller
  }


  private fun buildDraweeView(resources: Resources, loaderConfig: LoadConfig) {
    if (draweeHierarchyBuilder == null) {
      draweeHierarchyBuilder = GenericDraweeHierarchyBuilder(resources)
    }
    if (hierarchy == null) {
      hierarchy = draweeHierarchyBuilder?.build()
    }

    hierarchy?.let {
      if (loaderConfig.placeHolderRes != 0) {
        it.setPlaceholderImage(loaderConfig.placeHolderRes)
      }

      if (loaderConfig.failureRes != 0) {
        it.setFailureImage(loaderConfig.failureRes)
      }
    }
  }


  private fun resizeDraweeView(view: SimpleDraweeView, width: Int, height: Int) {
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



}