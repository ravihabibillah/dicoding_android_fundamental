package com.example.apigithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apigithubuserapp.api.ApiConfig
import com.example.apigithubuserapp.helper.Event
import com.example.apigithubuserapp.ItemsItem
import com.example.apigithubuserapp.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun findUser(user: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListUsers(user)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listUser.value = response.body()?.items
                    if (response.body()?.totalCount == 0) {
                        _toastText.value = Event(NONE)
                    } else {
                        _toastText.value = Event(SUCCESS)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _toastText.value = Event(FAILED)
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _toastText.value = Event(FAILED)

            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val SUCCESS = "Berhasil melakukan pencarian"
        private const val FAILED = "Gagal melakukan pencarian"
        private const val NONE = "Data tidak ditemukan"
    }
}