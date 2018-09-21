package com.taoszu.imageloader.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.taoszu.imageloader.*
import com.taoszu.imageloader.fresco.FrescoLoader
import com.taoszu.imageloader.glide.GlideLoader
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Fresco.initialize(this)
    setContentView(R.layout.activity_main)


    val frescoUriString = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537586212&di=b3c47e0d9ad3ffd7a0d39451e6b695cc&imgtype=jpg&er=1&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205172346_TjxGy.thumb.700_0.png"



    load_fresco.setOnClickListener {
      ImageLoaderManager.injectLoader(FrescoLoader()).init(this)

/*      ImageLoaderManager.loadUri(fresco_view, frescoUriString, object : ImageInfoCallback {
        val maxSize = ImageTools.dp2px(fresco_view.context, 360f)

        override fun onFailed() {}

        override fun onSuccess(imageSize: ImageSize) {
          var originHeight = imageSize.height
          var originWidth = imageSize.width

          var resultHeight:Int
          var resultWidth:Int

          if (originWidth > originHeight) {
            resultWidth = maxSize.toInt()
            resultHeight = (originHeight * (maxSize / originWidth)).toInt()
          } else {
            resultHeight = maxSize.toInt()
            resultWidth = (originWidth * (maxSize / originHeight)).toInt()
          }

          ImageTools.resizeView(fresco_view, resultWidth, resultHeight)
        }
      })*/
    }

    load_glide.setOnClickListener {
      ImageLoaderManager.injectLoader(GlideLoader()).init(this)
      ImageLoaderManager.loadUri(glide_view, frescoUriString)
    }

  }



}
