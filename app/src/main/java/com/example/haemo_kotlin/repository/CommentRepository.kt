package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class CommentRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getCommentListByPId(pId: Int, type: Int): Response<List<CommentResponseModel>> {
        var service = retrofitClient.service.getCommentListByPId(pId)

        service = when(type){
            1 ->{
                retrofitClient.service.getCommentListByPId(pId)
            }

            2 -> {
                retrofitClient.service.getCommentListByPId(pId)
            }

            else ->{
                retrofitClient.service.getCommentListByPId(pId)
            }
        }

        return service
    }

    suspend fun getCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getCommentUserList(pId)
    }
}