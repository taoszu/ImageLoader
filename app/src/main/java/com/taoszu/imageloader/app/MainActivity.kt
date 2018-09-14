package com.taoszu.imageloader.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.taoszu.imageloader.ImageLoaderManager
import com.taoszu.imageloader.LoadConfig
import com.taoszu.imageloader.fresco.FrescoLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ImageLoaderManager.injectLoader(FrescoLoader(this))

    setContentView(R.layout.activity_main)


    val uriString = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536899068672&di=8bc353d2115a856a8e78cba4ae49fb94&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20140105%2F20140105104029-1314816983.jpg"
    ImageLoaderManager.load(
            fresco_view, uriString,
            LoadConfig.Builder()
               .placeHolder(R.drawable.ic_launcher_background)
               .failure(R.mipmap.error)
               .isWrapContent(true)
               .build()

    )

  }
}
