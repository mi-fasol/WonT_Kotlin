package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.user.LoginModel
import com.example.haemo_kotlin.model.user.UserModel
import com.example.haemo_kotlin.model.user.UserResponseModel
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

    suspend fun getUserInfoById(uId: Int) :Response<UserResponseModel>{
        return retrofitClient.service.getUserInfoById(uId)
    }

    suspend fun getUserByNickname(nickname:String) :Response<UserResponseModel>{
        return retrofitClient.service.getUserByNickname(nickname)
    }

}