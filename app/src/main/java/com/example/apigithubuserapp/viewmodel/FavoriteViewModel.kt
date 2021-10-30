package com.example.apigithubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.apigithubuserapp.database.FavoriteUser
import com.example.apigithubuserapp.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> =
        mFavoriteRepository.getAllFavoriteUser()

    fun getByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteRepository.getUserByUsername(username)

}