package com.chenxuan.plugin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.chenxuan.hook.Catch
import com.chenxuan.hook.Track
import com.chenxuan.hook.sample.SampleUtil
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trackMethod()
        loadPic()
        hookThread()
        SampleUtil.sample()
    }

    @Catch
    private fun catchVoid() {
        val date = Date()
    }

    @Catch
    private fun catchObject() = Date()

    private fun hookThread() {
        val coreSize = Runtime.getRuntime().availableProcessors() + 1
        val fix = Executors.newFixedThreadPool(coreSize)
        val single = Executors.newSingleThreadExecutor()
        val cache = Executors.newCachedThreadPool()
        val scheduled = Executors.newScheduledThreadPool(coreSize)
    }

    private fun loadPic() {
        Glide.with(this)
                .load("https://pic.3gbizhi.com/2014/0430/20140430043839656.jpg")
                .into(findViewById(R.id.ivAvatar))
    }

    @Track
    private fun trackMethod() {
        val data = mutableListOf<String>()
    }
}