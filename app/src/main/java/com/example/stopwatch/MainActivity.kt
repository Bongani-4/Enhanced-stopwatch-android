package com.example.stopwatch


import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat



class MainActivity : AppCompatActivity() {

    private var isRunning = false
    private var seconds = 0
    private var isOnHold = false

    private lateinit var tvElapsedTime: TextView
    private var startTimeMillis: Long = 0
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var muteImageView: ImageView
    private var isMuted = true





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //music in the background
        mediaPlayer = MediaPlayer.create(this, R.raw.background_song)
        mediaPlayer?.isLooping = true



        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val btnHold = findViewById<Button>(R.id.btnHold)
        tvElapsedTime = findViewById(R.id.tvElapsedTime)

        // Mute ImageView
        muteImageView = findViewById(R.id.mute)

        muteImageView.setOnClickListener {
            // Check if music is currently playing
            if (isRunning) {
                isOnHold = !isOnHold
                if (isOnHold) {

                    isMuted = true

                    muteImageView.setImageResource(R.drawable.mutes)



                    mediaPlayer?.pause()
                } else {
                    // Resume the stopwatch by posting the runnable again

                    mediaPlayer?.start()
                    isMuted = false
                    muteImageView.setImageResource(R.drawable.louds)


                }
            }
            toggleMute()
        }



        //setting colors
        setStatusBarColor(R.color.blue)
        btnStart.setBackgroundColor(Color.CYAN);
        btnHold.setBackgroundColor(Color.CYAN);
        btnStop.setBackgroundColor(Color.CYAN);


        btnStart.setOnClickListener {
            startStopwatch()
        }

        btnStop.setOnClickListener {
            stopStopwatch()
        }



        btnHold.setOnClickListener {
            holdStopwatch()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }




    //status bar color
    private fun setStatusBarColor(colorResId: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, colorResId)
    }




    private fun updateTimer() {
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedMillis = currentTimeMillis - startTimeMillis

        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = ((elapsedMillis % 3600000) / 60000).toInt()
        val secondsPart = ((elapsedMillis % 60000) / 1000).toInt()
        val millisecondsPart = (elapsedMillis % 1000).toInt()

        val elapsedTimeText = String.format("%02d:%02d:%02d:%03d", hours, minutes, secondsPart, millisecondsPart)
        tvElapsedTime.text = " $elapsedTimeText"
    }

    private fun startStopwatch() {
        //background music
        isMuted = false
        muteImageView.setImageResource(R.drawable.louds)
        isRunning = true

        startTimeMillis = System.currentTimeMillis()
        handler.postDelayed(runnable, 16) // Update every 16 milliseconds (approx. 60 FPS)

    }
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                updateTimer()
                handler.postDelayed(this, 16) // Continue updating every 16 milliseconds


                //layout design colors
                val topBox = findViewById<View>(R.id.topBOx)
                val leftBox = findViewById<View>(R.id.topLeft)
                val rightBox = findViewById<View>(R.id.topRight)

                topBox.setBackgroundColor(ContextCompat.getColor(topBox.context, android.R.color.holo_green_light))
                leftBox.setBackgroundColor(ContextCompat.getColor(leftBox.context, android.R.color.holo_green_light))
                rightBox.setBackgroundColor(ContextCompat.getColor(rightBox.context, android.R.color.holo_green_light))
                 mediaPlayer?.start()
            }
        }
    }
    private fun stopStopwatch() {
        if (isRunning) {
            //background music
            isMuted = true
            muteImageView.setImageResource(R.drawable.mutes)
            isRunning = false


            handler.removeCallbacks(runnable) // Remove the callback to stop updating


            //layout design colors
            val topBox = findViewById<View>(R.id.topBOx)
            val leftBox = findViewById<View>(R.id.topLeft)
            val rightBox = findViewById<View>(R.id.topRight)

            topBox.setBackgroundColor(ContextCompat.getColor(topBox.context, android.R.color.holo_red_dark))
            leftBox.setBackgroundColor(ContextCompat.getColor(leftBox.context, android.R.color.holo_red_dark))
            rightBox.setBackgroundColor(ContextCompat.getColor(rightBox.context, android.R.color.holo_red_dark))


            mediaPlayer?.pause()
        }
    }

    private fun holdStopwatch() {
        //layout design colors
        val topBox = findViewById<View>(R.id.topBOx)
        val leftBox = findViewById<View>(R.id.topLeft)
        val rightBox = findViewById<View>(R.id.topRight)

        topBox.setBackgroundColor(ContextCompat.getColor(topBox.context, android.R.color.holo_orange_light))
        leftBox.setBackgroundColor(ContextCompat.getColor(leftBox.context, android.R.color.holo_orange_light))
        rightBox.setBackgroundColor(ContextCompat.getColor(rightBox.context, android.R.color.holo_orange_light))




        if (isRunning) {
            isOnHold = !isOnHold
            if (isOnHold) {

                isMuted = true
                muteImageView.setImageResource(R.drawable.mutes)



                handler.removeCallbacks(runnable) // Remove the callback to stop updating temporarily
                mediaPlayer?.pause()
            } else {
                // Resume the stopwatch by posting the runnable again
                handler.postDelayed(runnable, 16)
                mediaPlayer?.start()
                isMuted = false
                muteImageView.setImageResource(R.drawable.louds)



            }


        }
    }
    //inter changing mute and loud icons
    private fun toggleMute() {


        if (isMuted) {
            // If muted, change image to loud
            muteImageView.setImageResource(R.drawable.louds)
            mediaPlayer?.start()
        } else {
            // If not muted, change image to mute
            muteImageView.setImageResource(R.drawable.mutes)
            mediaPlayer?.pause()
        }
    }



}
