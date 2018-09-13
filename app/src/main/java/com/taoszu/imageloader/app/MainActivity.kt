package com.taoszu.imageloader.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.taoszu.imageloader.ImageLoaderManager
import com.taoszu.imageloader.LoadConfig
import com.taoszu.imageloader.fresco.FrescoLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ImageLoaderManager.injectLoader(FrescoLoader(this))

    setContentView(R.layout.activity_main)


    val uriString = "https://static.aicoinstorge.com/talk/18-09-13/201303-cdc-985.jpeg"
    ImageLoaderManager.load(fresco_view, uriString, LoadConfig.Builder().placeHolder(R.drawable.ic_launcher_background).build())

  }
}
