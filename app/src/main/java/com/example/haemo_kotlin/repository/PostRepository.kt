package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.PostModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getPost(): Response<List<PostModel>> {
        return retrofitClient.service.getPost()
    }

    suspend fun getOnePost(idx: Int): Response<PostModel> {
        return retrofitClient.service.getOnePost(idx)
    }

}