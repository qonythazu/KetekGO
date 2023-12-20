package com.dicoding.ketekgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class UserViewModel : ViewModel() {
    private val _filePhoto = MutableLiveData<File>()
    val filePhoto: LiveData<File> = _filePhoto

    private val _cameraSelector = MutableLiveData<Boolean>()
    val cameraSelector: LiveData<Boolean> = _cameraSelector

    private val _cameraCode = MutableLiveData<Int>()
    val cameraCode: LiveData<Int> = _cameraCode

    private val userIdLiveData = MutableLiveData<String>()
    fun setUserId(userId: String) {
        userIdLiveData.value = userId
    }

    fun getUserId(): LiveData<String> {
        return userIdLiveData
    }

    fun setFilePhoto(file: File) {
        _filePhoto.postValue(file)
    }

    fun setCameraSelector(camera: Boolean) {
        _cameraSelector.postValue(camera)
    }

    fun setCameraCode(code: Int) {
        _cameraCode.postValue(code)
    }
}