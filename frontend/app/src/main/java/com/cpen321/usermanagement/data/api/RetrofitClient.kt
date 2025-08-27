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

    val authService: AuthService = retrofit.create(AuthService::class.java)
    val userService: UserService = retrofit.create(UserService::class.java)
    val hobbyService: HobbyService = retrofit.create(HobbyService::class.java)


    fun setAuthToken(token: String?) {
        authToken = token
    }


    fun getImageUrl(imagePath: String): String {
        return if (!imagePath.startsWith("http")) {
            IMAGE_BASE_URL + imagePath
        } else {
            imagePath
        }
    }
}