package com.taoszu.imageloader.fresco

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.net.Uri
import android.support.annotation.WorkerThread
import android.view.View
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.taoszu.imageloader.ImageLoaderFrame
import com.taoszu.imageloader.ImageTools
import com.taoszu.imageloader.LoadConfig
import java.util.concurrent.CountDownLatch


class FrescoLoader : ImageLoaderFrame {

  @WorkerThread
  override fun getBitmap(context: Context, uriString: String): Bitmap? {
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

  override fun loadRes(view: View, resId: Int) {
    val draweeView = transformDraweeView(view)
    draweeView.setBackgroundResource(resId)
  }

  override fun loadUri(view: View, uriString: String, loaderConfig: LoadConfig) {
    val draweeView = transformDraweeView(view)
    buildDraweeViewConfig(draweeView, loaderConfig)

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
          val maxHeight = ImageTools.getMaxHeight(draweeView)
          val maxWidth = ImageTools.getMaxWidth(draweeView)
          val width = Math.min(maxWidth, imageInfo.width)
          val height = Math.min(maxHeight, imageInfo.height)

          ImageTools.resizeView(draweeView, width, height)
        }
      }

      override fun onFailure(id: String?, throwable: Throwable?) {
        super.onFailure(id, throwable)

        loaderConfig.takeIf {
          it.failureRes != 0 && it.isWrapContent
        }?.let {
          val failureBitmap = BitmapFactory.decodeResource(draweeView.resources, it.failureRes)
          ImageTools.resizeView(draweeView, failureBitmap.width, failureBitmap.height)
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


  private fun buildDraweeViewConfig(draweeView: SimpleDraweeView, loaderConfig: LoadConfig) {
    val hierarchy = draweeView.hierarchy
    if (loaderConfig.placeHolderRes != 0) {
      hierarchy.setPlaceholderImage(loaderConfig.placeHolderRes)
    }
    if (loaderConfig.failureRes != 0) {
      hierarchy.setFailureImage(loaderConfig.failureRes)
    }
  }

  private fun transformDraweeView(view: View): SimpleDraweeView {
    return view as SimpleDraweeView
  }

}