package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.comment.club.ClubCommentResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class CommentRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getCommentListByPId(pId: Int): Response<List<CommentResponseModel>> {
        return retrofitClient.service.getCommentListByPId(pId)
    }

    suspend fun getCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getCommentUserList(pId)
    }



    // Club Comment
    suspend fun getClubCommentListByPId(pId: Int): Response<List<ClubCommentResponseModel>> {
        return retrofitClient.service.getClubCommentListByPId(pId)
    }

    suspend fun getClubCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getClubCommentUserList(pId)
    }

}