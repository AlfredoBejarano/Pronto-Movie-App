package me.alfredobejarano.prontomovieapp.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import me.alfredobejarano.prontomovieapp.R
import me.alfredobejarano.prontomovieapp.databinding.ActivityMainBinding
import me.alfredobejarano.prontomovieapp.utils.EventManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(this, R.raw.blop)
        subscribeToEvents()
        setContentView(R.layout.activity_main)
    }

    private fun subscribeToEvents() = EventManager.run {
        playFavoriteSoundLiveData.observe(this@MainActivity, Observer { mediaPlayer.start() })
    }
}
