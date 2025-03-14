package com.example.mostaqarapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
//import androidx.media3.common.MimeTypes
//import androidx.media3.datasource.DefaultDataSource
//import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.mostaqarapp.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeVideoActivity : AppCompatActivity() {

    private var player1: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playBackPosition: Long = 0
    private lateinit var videoUrl: String
    private lateinit var video: PlayerView
    var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_video)

        var auth = Firebase.auth
        video = findViewById<PlayerView>(R.id.video)
        val tvVideo = findViewById<TextView>(R.id.tvVideo)
        videoUrl = intent?.getStringExtra("videoUrl").toString()

        videoUrl = intent?.getStringExtra("videoUrl").toString()
        Toast.makeText(this, videoUrl.toString(), Toast.LENGTH_SHORT).show()
        if (videoUrl == null || videoUrl== "") {
            tvVideo.visibility = View.VISIBLE
            video.visibility = View.INVISIBLE
        } else {
            Toast.makeText(this, videoUrl, Toast.LENGTH_SHORT).show()
            initVideo()
        }

    }

    private fun initVideo() {
        player1 = SimpleExoPlayer.Builder(this).build()
        video.player = player1

        if (videoUrl != null || videoUrl!="null") {
            val mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build()
            val dataSourceFactory = DefaultDataSource.Factory(this)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)

            player1?.playWhenReady = playWhenReady
            player1?.seekTo(currentWindow, playBackPosition)
            player1?.setMediaSource(mediaSource)
            player1?.prepare()
        } else {
            val msg = findViewById<TextView>(R.id.tvVideo)
            msg.visibility = View.VISIBLE
            video.visibility = View.INVISIBLE
        }
    }

    private fun releaseVideo() {
        player1?.let {
            playWhenReady = it.playWhenReady
            playBackPosition = it.currentPosition
            currentWindow = it.currentWindowIndex
            it.release()
            player1 = null
        }
    }

    override fun onPause() {
        super.onPause()
        releaseVideo()
    }

    override fun onStart() {
        super.onStart()
        initVideo()
    }

}