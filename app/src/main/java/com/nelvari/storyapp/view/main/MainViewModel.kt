package com.nelvari.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.pref.UserModel
import com.nelvari.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel (private val repository: Repository) : ViewModel() {

    val getStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}