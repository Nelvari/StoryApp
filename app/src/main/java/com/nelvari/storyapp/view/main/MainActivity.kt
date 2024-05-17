package com.nelvari.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.nelvari.storyapp.R
import com.nelvari.storyapp.view.adapter.StoryAdapter
import com.nelvari.storyapp.databinding.ActivityMainBinding
import com.nelvari.storyapp.view.welcome.WelcomeActivity
import com.nelvari.storyapp.view.ViewModelFactory
import com.nelvari.storyapp.view.adapter.LoadingStateAdapter
import com.nelvari.storyapp.view.add.AddActivity
import com.nelvari.storyapp.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        getData()

        val layoutManager = GridLayoutManager(this, 2)
        binding.rvStory.layoutManager = layoutManager

        setupAction()

    }

    private fun setupAction() {

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.menu2 -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu3 -> {
                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle("Log Out")
                        setMessage("Yakin ingin keluar?")
                        setPositiveButton("Ok") { _, _ ->
                            viewModel.logout()
                        }
                        setNegativeButton("Cancel") { dialog, _ ->
                            dialog.cancel()
                        }
                        create()
                        show()
                    }
                    true
                }
                else -> false
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.getStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }


}