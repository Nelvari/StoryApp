package com.nelvari.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.nelvari.storyapp.data.pref.UserModel
import com.nelvari.storyapp.data.pref.UserPreference
import com.nelvari.storyapp.data.response.DetailResponse
import com.nelvari.storyapp.data.response.ListStoryItem
import com.nelvari.storyapp.data.response.ResultResponse
import com.nelvari.storyapp.data.response.StoryResponse
import com.nelvari.storyapp.data.retrofit.ApiConfig
import com.nelvari.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStory() : LiveData<PagingData<ListStoryItem>>{
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Pager(
            config = PagingConfig(
                pageSize = 6
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getDetailStory(id: String) : DetailResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getDetailStory(id)
    }

    suspend fun uploadImage(file: MultipartBody.Part, desc: RequestBody) : ResultResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.uploadImage(file, desc)
    }

    suspend fun register(name: String, email: String, password: String) : ResultResponse {
        return apiService.register(name, email, password)
    }

    suspend fun getStoriesWithLocation() : StoryResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getStoriesWithLocation()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}