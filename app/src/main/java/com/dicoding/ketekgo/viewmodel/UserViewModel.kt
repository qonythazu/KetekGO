package com.dicoding.ketekgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val userIdLiveData = MutableLiveData<String>()
    fun setUserId(userId: String) {
        userIdLiveData.value = userId
    }

    fun getUserId(): LiveData<String> {
        return userIdLiveData
    }
}