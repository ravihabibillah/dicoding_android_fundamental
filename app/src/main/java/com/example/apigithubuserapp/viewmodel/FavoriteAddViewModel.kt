package com.example.apigithubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.apigithubuserapp.database.FavoriteUser
import com.example.apigithubuserapp.repository.FavoriteRepository

class FavoriteAddViewModel(application: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteRepository.insert(favoriteUser)
    }

    fun delete(username: String) {
        mFavoriteRepository.delete(username)
    }


}