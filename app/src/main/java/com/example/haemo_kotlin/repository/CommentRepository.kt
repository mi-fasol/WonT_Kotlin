package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.comment.CommentModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.comment.club.ClubCommentModel
import com.example.haemo_kotlin.model.comment.club.ClubCommentResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import org.w3c.dom.Comment
import retrofit2.Response
import javax.inject.Inject

class CommentRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getCommentListByPId(pId: Int): Response<List<CommentResponseModel>> {
        return retrofitClient.service.getCommentListByPId(pId)
    }

    suspend fun getCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getCommentUserList(pId)
    }

    suspend fun saveComment(comment: CommentModel): Response<CommentResponseModel> {
        return retrofitClient.service.registerPostComment(comment)
    }

    // Club Comment
    suspend fun getClubCommentListByPId(pId: Int): Response<List<CommentResponseModel>> {
        return retrofitClient.service.getClubCommentListByPId(pId)
    }

    suspend fun getClubCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getClubCommentUserList(pId)
    }

    suspend fun saveClubComment(comment: CommentModel): Response<CommentResponseModel> {
        return retrofitClient.service.registerClubComment(comment)
    }

    // HotPlace Comment
    suspend fun getHotPlaceCommentListByPId(pId: Int): Response<List<CommentResponseModel>> {
        return retrofitClient.service.getHotPlaceCommentListByPId(pId)
    }

    suspend fun getHotPlaceCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getHotPlaceCommentUserList(pId)
    }

    suspend fun saveHotPlaceComment(comment: CommentModel): Response<CommentResponseModel> {
        return retrofitClient.service.registerHotPlaceComment(comment)
    }

}