package com.example.apigithubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.apigithubuserapp.database.FavoriteDao
import com.example.apigithubuserapp.database.FavoriteRoomDatabase
import com.example.apigithubuserapp.database.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }

    fun delete(username: String) {
        executorService.execute { mFavoriteDao.delete(username) }
    }

    fun getUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteDao.getUserWithUsername(username)


}