package com.taoszu.imageloader.fresco

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.taoszu.imageloader.ImageLoaderStrategy
import com.taoszu.imageloader.LoadConfig

class FrescoLoader(context:Context) : ImageLoaderStrategy {

  init {
    Fresco.initialize(context.applicationContext)
  }

  private var draweeHierarchyBuilder:GenericDraweeHierarchyBuilder? = null
  private var hierarchy : GenericDraweeHierarchy?= null

  override fun load(view: View, uriString: String, loaderConfig: LoadConfig) {
    buildDraweeView(view.resources, loaderConfig)

    val draweeView = view as SimpleDraweeView
    draweeView.hierarchy = hierarchy
    draweeView.setImageURI(uriString)
  }


  private fun buildDraweeView(resources:Resources, loaderConfig: LoadConfig) {
    if (draweeHierarchyBuilder == null) {
      draweeHierarchyBuilder = GenericDraweeHierarchyBuilder(resources)
    }
    if (hierarchy == null) {
      hierarchy = draweeHierarchyBuilder?.build()
    }
    hierarchy?.setPlaceholderImage(loaderConfig.placeHolderRes)
  }

}