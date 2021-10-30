package com.example.apigithubuserapp.api

import com.example.apigithubuserapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            // Saat menggunakan logging interceptor, pastikan kembali pesan log hanya akan tampil
            // pada mode debug. Saat informasi sensitif dapat mudah lihat di jendela logcat dan
            // ini membuat penerapan security menyebabkan vuln di mana data yang tampil dapat
            // dimanfaatkan oleh pihak yang tidak bertanggung jawab.
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit= Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}