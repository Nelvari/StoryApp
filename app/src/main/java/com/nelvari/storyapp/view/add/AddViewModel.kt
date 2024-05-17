package com.nelvari.storyapp.view.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.response.ResultResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddViewModel (private val repository: Repository) : ViewModel() {

    private val _result = MutableLiveData<ResultResponse>()
    val result: LiveData<ResultResponse> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "AddViewModel"
    }

    fun uploadImage(file: MultipartBody.Part, desc: RequestBody) {
        _isLoading.value = true

        viewModelScope.launch {

            try {
                _result.value = repository.uploadImage(file, desc)
                _isLoading.value = false
            }catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResultResponse::class.java)
                Log.e(TAG, "upload: $errorResponse")
                _result.value = errorResponse
                _isLoading.value = false
            }

        }

    }

}