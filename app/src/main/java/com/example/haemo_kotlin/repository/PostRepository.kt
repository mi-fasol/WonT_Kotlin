package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getPost(): Response<List<PostModel>> {
        return retrofitClient.service.getPost()
    }

    suspend fun getTodayPost(): Response<List<PostModel>> {
        return retrofitClient.service.getTodayPost()
    }

    suspend fun getOnePost(idx: Int): Response<PostModel> {
        return retrofitClient.service.getOnePost(idx)
    }

    suspend fun getPostingUser(pId: Int): Response<UserResponseModel> {
        return retrofitClient.service.getPostingUser(pId)
    }


    suspend fun getClubPost(): Response<List<ClubPostModel>> {
        return retrofitClient.service.getClubPost()
    }

    suspend fun getOneClubPost(idx: Int): Response<ClubPostModel> {
        return retrofitClient.service.getClubPostById(idx)
    }

    suspend fun getHotPlacePost(): Response<List<HotPlacePostModel>> {
        return retrofitClient.service.getHotPlacePost()
    }
    suspend fun getHotPlaceById(idx: Int): Response<HotPlacePostModel> {
        return retrofitClient.service.getHotPlacePostById(idx)
    }
    suspend fun getPopularHotPlace(): Response<List<HotPlacePostModel>> {
        return retrofitClient.service.getPopularHotPlacePost()
    }
}