package com.cpen321.usermanagement.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private const val IMAGE_BASE_URL = "http://10.0.2.2:3000/"

    private var authToken: String? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val authInterceptor = AuthInterceptor { authToken }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    
    /**
     * Sets the authentication token to be used for API requests
     * @param token The authentication token
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }

    /**
     * Constructs the full URL for an image by combining the image base URL with the image path
     * @param imagePath The image path returned from the backend (e.g., /uploads/profile-pictures/filename.jpg)
     * @return The complete URL that can be used to display the image
     */
    fun getImageUrl(imagePath: String): String {
        return if (!imagePath.startsWith("http")) {
            IMAGE_BASE_URL + imagePath
        } else {
            imagePath
        }
    }
}
