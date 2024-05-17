package com.nelvari.storyapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.response.ResultResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel (private val repository: Repository) : ViewModel() {

    private val _result = MutableLiveData<ResultResponse>()
    val result: LiveData<ResultResponse> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "SignupViewModel"
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {

            try {
                _result.value = repository.register(name, email, password)
                _isLoading.value = false
            }catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResultResponse::class.java)
                Log.e(TAG, "sign: $errorResponse")
                Log.e(TAG, "sign: $errorBody")
                _result.value = errorResponse
                _isLoading.value = false
            }

        }

    }

}