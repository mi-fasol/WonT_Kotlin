package com.example.haemo_kotlin.repository

import android.net.http.UrlRequest.Status
import com.example.haemo_kotlin.model.LoginModel
import com.example.haemo_kotlin.model.PostModel
import com.example.haemo_kotlin.model.UserModel
import com.example.haemo_kotlin.model.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun tryLogin(loginModel: LoginModel): Response<Boolean> {
        return retrofitClient.service.tryLogin(loginModel)
    }

    suspend fun registerUser(user: UserModel): Response<UserResponseModel> {
        return retrofitClient.service.registerUser(user)
    }

}