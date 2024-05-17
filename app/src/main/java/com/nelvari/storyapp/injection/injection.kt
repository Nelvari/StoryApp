package com.nelvari.storyapp.injection

import android.content.Context
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.pref.UserPreference
import com.nelvari.storyapp.data.pref.dataStore
import com.nelvari.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(apiService, pref)
    }
}