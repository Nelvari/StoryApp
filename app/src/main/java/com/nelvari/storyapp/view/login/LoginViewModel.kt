package com.nelvari.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nelvari.storyapp.data.Repository
import com.nelvari.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: Repository) : ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}