package me.alfredobejarano.prontomovieapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.alfredobejarano.prontomovieapp.R
import me.alfredobejarano.prontomovieapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
