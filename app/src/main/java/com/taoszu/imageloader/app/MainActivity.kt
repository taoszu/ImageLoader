package com.taoszu.imageloader.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.taoszu.imageloader.ImageLoaderManager
import com.taoszu.imageloader.LoadConfig
import com.taoszu.imageloader.fresco.FrescoLoader
import com.taoszu.imageloader.glide.GlideLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Fresco.initialize(this)
    setContentView(R.layout.activity_main)


    val glideUriString = "http://img5.imgtn.bdimg.com/it/u=3535477663,205047880&fm=26&gp=0.jpg"
    val frescoUriString = "http://img1.imgtn.bdimg.com/it/u=71655007,1152159672&fm=26&gp=0.jpg"


    load_fresco.setOnClickListener {
      ImageLoaderManager.injectLoader(FrescoLoader())
      ImageLoaderManager.loadUri(
              fresco_view, frescoUriString,
              LoadConfig.Builder()
                      .placeHolder(R.drawable.ic_launcher_background)
                      .failure(R.mipmap.error)
                      .isWrapContent(false)
                      .build()
      )
    }

    load_glide.setOnClickListener {
      ImageLoaderManager.injectLoader(GlideLoader())
      ImageLoaderManager.loadUri(
              glide_view, glideUriString,
              LoadConfig.Builder()
                      .placeHolder(R.drawable.ic_launcher_background)
                      .failure(R.mipmap.error)
                      .isWrapContent(false)
                      .build())
    }


  }


}
