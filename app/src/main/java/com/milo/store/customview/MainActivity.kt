package com.milo.store.customview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import com.milo.store.customview.custom_view.ColorTrackTextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val viewTrackColor = findViewById<ColorTrackTextView?>(R.id.color_track_view)
//        val anim = ObjectAnimator.ofFloat(viewTrackColor, "progress",0F,1F)
//        anim.apply {
//            duration = 3000
//            interpolator = AccelerateDecelerateInterpolator()
//            repeatMode = ValueAnimator.REVERSE
//            repeatCount = -1
//            start()
//        }
        Log.e("ABC", "onCreate: 1 ", )
//        execute()
        Log.e("ABC", "onCreate: 2 ", )

    }
    fun test (){
        Log.e("ABC", "test: 3", )
        val url = "https://stores.volio.vn/stores/api/v5.0/public/items?category_id=3b0ce74a-e062-4c45-9396-cf11e0845c41&offset=0&limit=50&region_code=vn"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
//        val data = connection.inputStream.bufferedReader().readText()
//        Log.e("ABC", "test: ${data} ", )
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            Log.e("ABC", "test: ${connection.readJsonFromRequest()} ", )
        } else {
            throw Exception("Unexpected response code ${connection.responseCode}")
        }
    }

    fun execute(){
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            test()
        }
    }
}

private fun HttpURLConnection.readJsonFromRequest(): String {
    val content = StringBuilder("")
    val inputStreamReader = InputStreamReader(this.inputStream, Charsets.UTF_8)
    val bufferedReader = BufferedReader(inputStreamReader)
    bufferedReader.forEachLine {
        content.append(it)
    }
    inputStreamReader.close()
    return content.toString()
}