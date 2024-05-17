package com.nelvari.storyapp.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.response.ResultResponse
import com.nelvari.storyapp.data.response.Story
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel (private val repository: Repository) : ViewModel() {

    private val _detail = MutableLiveData<Story>()
    val detail: LiveData<Story> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun findDetail(id: String) {
        _isLoading.value = true

        viewModelScope.launch {

            try {
                _detail.value = repository.getDetailStory(id).story
                _isLoading.value = false
            }catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResultResponse::class.java)
                Log.e(TAG, "getStory: $errorResponse")
                _isLoading.value = false
            }

        }

    }

}