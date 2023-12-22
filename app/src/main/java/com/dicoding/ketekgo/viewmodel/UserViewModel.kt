package com.dicoding.ketekgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.ketekgo.data.AppRepository
import com.dicoding.ketekgo.data.Result
import com.dicoding.ketekgo.dataclass.DestinationResponse
import com.dicoding.ketekgo.dataclass.PreferencesRequest
import java.io.File

class UserViewModel(private val repos: AppRepository) : ViewModel() {
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

    fun recomendDestination(preferences: PreferencesRequest): LiveData<Result<DestinationResponse>> {
        return repos.recomendDestination(preferences)
    }
}