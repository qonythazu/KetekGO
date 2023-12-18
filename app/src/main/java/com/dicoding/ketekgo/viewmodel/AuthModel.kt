package com.dicoding.ketekgo.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.ketekgo.data.repository.AppRepository

class AuthModel(private val repos: AppRepository) : ViewModel() {
    fun authLogin(email: String, password: String) = repos.loginAuth(email, password)
}