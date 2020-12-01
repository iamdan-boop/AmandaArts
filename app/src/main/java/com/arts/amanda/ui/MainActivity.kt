package com.arts.amanda.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arts.amanda.R
import com.arts.amanda.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AmandaArts)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomBar()
    }

    private fun setBottomBar() {
      val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_view) as NavHostFragment
      val navController = navHostFragment.navController
      val appBarConfiguration = AppBarConfiguration(setOf(R.id.feedFragment, R.id.uploadFragment, R.id.profileFragment))
      setupActionBarWithNavController(navController, appBarConfiguration)
      binding.bottomNavigationView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}