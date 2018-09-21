package com.taoszu.imageloader.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.taoszu.imageloader.ImageLoaderManager
import com.taoszu.imageloader.LoadOptions
import com.taoszu.imageloader.fresco.FrescoLoader
import com.taoszu.imageloader.glide.GlideLoader
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Fresco.initialize(this)
    setContentView(R.layout.activity_main)


    val frescoUriString = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537586212&di=b3c47e0d9ad3ffd7a0d39451e6b695cc&imgtype=jpg&er=1&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205172346_TjxGy.thumb.700_0.png"
    val uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537542549816&di=d9f0026752fd4f862372d88267720c63&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F7%2F587483dedc055.jpg"

    load_fresco.setOnClickListener {
      ImageLoaderManager.injectLoader(FrescoLoader()).init(this)
    }


    load_glide.setOnClickListener {
      ImageLoaderManager.injectLoader(GlideLoader()).init(this)
      ImageLoaderManager.loadUri(glide_view, uri,
              LoadOptions.Builder().progress(R.mipmap.widget_loading_frame_bg).
                      failure(R.mipmap.ic_launcher).build()
      )

    }

  }


}
