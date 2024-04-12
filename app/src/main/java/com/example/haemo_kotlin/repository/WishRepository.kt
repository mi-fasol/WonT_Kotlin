package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class WishRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getWishMeetingPost(uId: Int): Response<List<PostResponseModel>> {
        return retrofitClient.service.getWishMeetingPost(uId)
    }

    suspend fun getWishClubPost(uId: Int): Response<List<ClubPostResponseModel>> {
        return retrofitClient.service.getWishClubPost(uId)
    }

    suspend fun getWishHotPlacePost(uId: Int): Response<List<HotPlaceResponsePostModel>> {
        return retrofitClient.service.getWishHotPlacePost(uId)
    }
}