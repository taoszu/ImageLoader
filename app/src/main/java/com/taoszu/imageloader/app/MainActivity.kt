package com.taoszu.imageloader.app

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.facebook.drawee.backends.pipeline.Fresco
import com.taoszu.imageloader.ImageLoaderManager
import com.taoszu.imageloader.callback.FileCallback
import com.taoszu.imageloader.config.LoadOptions
import com.taoszu.imageloader.config.RoundParams
import com.taoszu.imageloader.fresco.FrescoLoader
import com.taoszu.imageloader.glide.GlideLoader
import com.taoszu.imageloader.tool.ImageTools
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.file.FileSystem


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Fresco.initialize(this)
    setContentView(R.layout.activity_main)


    val frescoUriString = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537586212&di=b3c47e0d9ad3ffd7a0d39451e6b695cc&imgtype=jpg&er=1&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205172346_TjxGy.thumb.700_0.png"
    val uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537542549816&di=d9f0026752fd4f862372d88267720c63&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F7%2F587483dedc055.jpg"
    load_fresco.setOnClickListener {
      ImageLoaderManager.injectLoader(FrescoLoader()).init(this)

      val fileDir = Glide.getPhotoCacheDir(this)
      fileDir?.let {
        val fileList = it.listFiles()
        var totalSize = 0L
        for (i in 0 until fileList.size) {
          totalSize += fileList[i].length()
        }
        Log.e("File", totalSize.toString())
      }

      ImageLoaderManager.loadUri(fresco_view, uri)
    }

    load_glide.setOnClickListener {
      ImageLoaderManager.injectLoader(GlideLoader()).init(this)

      ImageLoaderManager.requestFile(it.context, "https://www.jiuwa.net/tuku/20171101/XicyZO1H.gif", object: FileCallback {
        override fun onFailed() {
          Log.e("File", "load failed")
        }

        override fun onSuccess(file: File) {
          fresco_view.setImageURI(Uri.fromFile(file))
          Log.e("File", file.length().toString())
        }
      })

    }

  }



}
