package com.taoszu.imageloader.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.taoszu.imageloader.ImageLoaderManager
import com.taoszu.imageloader.LoadConfig
import com.taoszu.imageloader.fresco.FrescoLoader
import com.taoszu.imageloader.glide.GlideLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ImageLoaderManager.injectLoader(FrescoLoader(this))

    setContentView(R.layout.activity_main)


    // val uriString = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536901996262&di=6adde97fe73a8ab77ac1291820eb2482&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201503%2F07%2F20150307210829_8EFRu.jpeg"
    val uriString = "https://static.aicoinstorge.com/talk/18-09-14/102107-6ce-858.jpeg"



    ImageLoaderManager.injectLoader(FrescoLoader(this))
    ImageLoaderManager.load(
            fresco_view, uriString,
            LoadConfig.Builder()
                    .placeHolder(R.drawable.ic_launcher_background)
                    .failure(R.mipmap.error)
                    .isWrapContent(false)
                    .build()
    )



    ImageLoaderManager.injectLoader(GlideLoader())
    ImageLoaderManager.load(
            glide_view, uriString,
            LoadConfig.Builder()
                    .placeHolder(R.drawable.ic_launcher_background)
                    .failure(R.mipmap.error)
                    .isWrapContent(true)
                    .build()
    )


  }
}
