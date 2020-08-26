package me.alfredobejarano.prontomovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import me.alfredobejarano.prontomovieapp.R
import me.alfredobejarano.prontomovieapp.databinding.ActivityMainBinding
import me.alfredobejarano.prontomovieapp.utils.EventManager

class MainActivity : AppCompatActivity() {
    private lateinit var errorSnackBar: Snackbar
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        errorSnackBar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        setContentView(binding.root)
        subscribeToEvents()
        binding.mainBottomNavigationView.setupWithNavController(findNavController(R.id.main_fragment_container))
    }

    private fun subscribeToEvents() = EventManager.run {
        showLoadingLiveData.observe(this@MainActivity, Observer {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        })
        errorLiveData.observe(this@MainActivity, Observer {
            it?.run { errorSnackBar.setText(it).show() }
        })
    }
}
