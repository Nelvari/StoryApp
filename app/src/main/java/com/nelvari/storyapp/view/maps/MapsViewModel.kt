package com.nelvari.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.response.ListStoryItem
import com.nelvari.storyapp.data.response.ResultResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel (private val repository: Repository) : ViewModel() {

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

    fun getStoryWithLocation() {

        viewModelScope.launch {

            try {
                _story.value = repository.getStoriesWithLocation().listStory
            }catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResultResponse::class.java)
                Log.e(TAG, "maps: $errorResponse")
            }

        }

    }

    companion object {
        private const val TAG = "MapsViewModel"
    }

}