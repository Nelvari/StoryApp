package com.nelvari.storyapp.view.detail

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nelvari.storyapp.data.response.ListStoryItem
import com.nelvari.storyapp.data.response.Story
import com.nelvari.storyapp.databinding.ActivityDetailBinding
import com.nelvari.storyapp.view.ViewModelFactory
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("key", ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("key")
        }

        if (!isOnline(this)) {
            binding.tvName.text = data?.name
            binding.tvDesc.text = data?.description
            binding.tvDate.visibility = View.GONE
            Glide.with(this@DetailActivity)
                .load(data?.photoUrl)
                .into(binding.ivPicture)
        } else {

            if (data != null) {
                binding.tvName.text = data.name
                binding.tvDesc.text = data.description
                binding.tvDate.visibility = View.VISIBLE
                Glide.with(this@DetailActivity)
                    .load(data.photoUrl)
                    .into(binding.ivPicture)
                viewModel.findDetail(data.id)
                Log.e("Tag", "onFailures: ${data.description}")
            }

            viewModel.detail.observe(this) { story ->
                setDetailData(story)
            }

            viewModel.isLoading.observe(this) {
                showLoading(it)
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDetailData(detail: Story) {

        val utcDateTimeString = detail.createdAt

        val utcDateTime = LocalDateTime.parse(utcDateTimeString, DateTimeFormatter.ISO_DATE_TIME)

        val zoneIdWIB = ZoneId.of("Asia/Jakarta")
        val wibDateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneIdWIB).toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")
        val formattedDateTime = wibDateTime.format(formatter)

        binding.tvDate.text = formattedDateTime

    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

}