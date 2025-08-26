package com.cpen321.usermanagement.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.cpen321.usermanagement.config.AppConfig

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(AppConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(AppConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(AppConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(AppConfig.API_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    
    /**
     * Constructs the full URL for an image by combining the image base URL with the image path
     * @param imagePath The image path returned from the backend (e.g., /uploads/profile-pictures/filename.jpg)
     * @return The complete URL that can be used to display the image
     */
    fun getImageUrl(imagePath: String): String {
        return if (imagePath.startsWith("/")) {
            AppConfig.IMAGE_BASE_URL + imagePath
        } else {
            AppConfig.IMAGE_BASE_URL + "/" + imagePath
        }
    }
}
