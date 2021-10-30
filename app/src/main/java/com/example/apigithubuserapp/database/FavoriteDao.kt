package com.example.apigithubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

//    @Update
//    fun update(favoriteUser: FavoriteUser)

    @Query("DELETE FROM favoriteuser WHERE username = :username")
    fun delete(username: String?)

    @Query("SELECT * from favoriteUser ORDER BY id ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favoriteuser WHERE username = :username")
    fun getUserWithUsername(username: String?) : LiveData<FavoriteUser>
}