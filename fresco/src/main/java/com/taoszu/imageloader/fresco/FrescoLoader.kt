package com.taoszu.imageloader.fresco

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.net.Uri
import android.support.annotation.WorkerThread
import android.widget.ImageView
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.DraweeHolder
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.taoszu.imageloader.FrameConfig
import com.taoszu.imageloader.ImageLoaderFrame
import com.taoszu.imageloader.ImageTools
import com.taoszu.imageloader.LoadOptions
import java.util.concurrent.CountDownLatch


class FrescoLoader : ImageLoaderFrame() {

  override fun init(context: Context, frameConfig: FrameConfig) {
    val config = ImagePipelineConfig.newBuilder(context)
            .setBitmapMemoryCacheParamsSupplier {
              val MAX_CACHE_ENTRIES = 256
              val MAX_EVICTION_QUEUE_SIZE = Integer.MAX_VALUE
              val MAX_EVICTION_QUEUE_ENTRIES = Integer.MAX_VALUE
              val MAX_CACHE_ENTRY_SIZE = Integer.MAX_VALUE

              MemoryCacheParams(
                      frameConfig.memoryCacheSize.toInt(),
                      MAX_CACHE_ENTRIES, MAX_EVICTION_QUEUE_SIZE,
                      MAX_EVICTION_QUEUE_ENTRIES, MAX_CACHE_ENTRY_SIZE
              )
            }
            .build()

    Fresco.initialize(context.applicationContext, config)
  }

  override fun init(context: Context) {
    Fresco.initialize(context.applicationContext)
  }

  @WorkerThread
  override fun getBitmap(context: Context, uriString: String?): Bitmap? {
    var bitmap: Bitmap? = null
    val countDownLatch = CountDownLatch(1)

    val requestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uriString))
    val imageRequest = requestBuilder.build()
    val dataSource = ImagePipelineFactory.getInstance().imagePipeline.fetchDecodedImage(imageRequest, null)
    dataSource.subscribe(object : BaseBitmapDataSubscriber() {
      override fun onNewResultImpl(resultBitmap: Bitmap?) {
        bitmap = resultBitmap
        countDownLatch.countDown()
      }

      override fun onCancellation(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
        super.onCancellation(dataSource)
        countDownLatch.countDown()
      }

      override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
        countDownLatch.countDown()
      }
    }, UiThreadImmediateExecutorService.getInstance())

    countDownLatch.await()
    return bitmap
  }

  override fun loadRes(imageView: ImageView, resId: Int, loaderOptions: LoadOptions) {
    val uri = Uri.Builder().scheme("res").authority(imageView.context.packageName).appendPath(resId.toString()).build()
    loadUri(imageView, uri.toString(), loaderOptions)
  }

  override fun loadUri(imageView: ImageView, uriString: String?, loaderOptions: LoadOptions) {
    val controllerBuilder = Fresco.newDraweeControllerBuilder()
    if (loaderOptions.isWrapContent) {
      controllerBuilder.controllerListener = transformResizeListener(imageView, loaderOptions)
    }
    val draweeHolder = transformDraweeHolder(imageView, loaderOptions)
    draweeHolder.controller = controllerBuilder.setUri(uriString).build()
    imageView.setImageDrawable(draweeHolder.topLevelDrawable)
  }

  override fun clearMemoryCache(context: Context) {

  }

  override fun clearDiskCache(context: Context) {
  }


  private fun transformResizeListener(imageView: ImageView, loaderOptions: LoadOptions):BaseControllerListener<ImageInfo> {
    return object : BaseControllerListener<ImageInfo>() {
      override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        super.onFinalImageSet(id, imageInfo, animatable)
        imageInfo?.let {
          val maxHeight = ImageTools.getMaxHeight(imageView)
          val maxWidth = ImageTools.getMaxWidth(imageView)
          val width = Math.min(maxWidth, imageInfo.width)
          val height = Math.min(maxHeight, imageInfo.height)

          ImageTools.resizeView(imageView, width, height)
        }
      }

      override fun onFailure(id: String?, throwable: Throwable?) {
        super.onFailure(id, throwable)

        loaderOptions.takeIf {
          it.failureRes != 0 && it.isWrapContent
        }?.let {
          val failureBitmap = BitmapFactory.decodeResource(imageView.resources, it.failureRes)
          ImageTools.resizeView(imageView, failureBitmap.width, failureBitmap.height)
          failureBitmap.recycle()
        }
      }

    }
  }


  private fun transformDraweeHolder(imageView: ImageView, loaderOptions: LoadOptions): DraweeHolder<GenericDraweeHierarchy> {
    val imageTag = imageView.getTag(R.id.drawee_holder)
    return if (imageTag != null) {
      imageTag as DraweeHolder<GenericDraweeHierarchy>
    } else {
      val hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(imageView.context.resources)
      val hierarchy = hierarchyBuilder.build()
      buildDraweeViewConfig(hierarchy, loaderOptions)
      val draweeHolder = DraweeHolder.create(hierarchy, imageView.context)

      imageView.setTag(R.id.drawee_holder, draweeHolder)
      draweeHolder
    }
  }

  private fun buildDraweeViewConfig(hierarchy: GenericDraweeHierarchy, loaderOptions: LoadOptions) {
    if (loaderOptions.placeHolderRes != 0) {
      hierarchy.setPlaceholderImage(loaderOptions.placeHolderRes)
    }
    if (loaderOptions.failureRes != 0) {
      hierarchy.setFailureImage(loaderOptions.failureRes)
    }

    if (loaderOptions.asCircle) {
      val roundParams = RoundingParams.asCircle()
      hierarchy.roundingParams = roundParams
    }

    loaderOptions.roundParams?.let {
      val roundingParams = RoundingParams()
      roundingParams.setCornersRadius(it.radius)
      hierarchy.roundingParams = roundingParams
    }

  }


}