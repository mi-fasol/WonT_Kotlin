package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.comment.comment.CommentModel
import com.example.haemo_kotlin.model.comment.comment.CommentResponseModel
import com.example.haemo_kotlin.model.comment.reply.ReplyModel
import com.example.haemo_kotlin.model.comment.reply.ReplyResponseModel
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

    suspend fun saveComment(comment: CommentModel): Response<CommentResponseModel> {
        return retrofitClient.service.registerPostComment(comment)
    }

    suspend fun saveReply(reply: ReplyModel): Response<ReplyResponseModel> {
        return retrofitClient.service.registerPostReply(reply)
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

    suspend fun saveClubReply(reply: ReplyModel): Response<ReplyResponseModel> {
        return retrofitClient.service.registerClubPostReply(reply)
    }

    // HotPlace Comment, Reply
    suspend fun getHotPlaceCommentListByPId(pId: Int): Response<List<CommentResponseModel>> {
        return retrofitClient.service.getHotPlaceCommentListByPId(pId)
    }

    suspend fun getHotPlaceCommentUser(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getHotPlaceCommentUserList(pId)
    }

    suspend fun saveHotPlaceComment(comment: CommentModel): Response<CommentResponseModel> {
        return retrofitClient.service.registerHotPlaceComment(comment)
    }

    suspend fun saveHotPlaceReply(reply: ReplyModel): Response<ReplyResponseModel> {
        return retrofitClient.service.registerHotPlacePostReply(reply)
    }

    // 대댓글

    suspend fun getReplyListByCId(cId: Int): Response<List<ReplyResponseModel>> {
        return retrofitClient.service.getReplyByCId(cId)
    }

    suspend fun getClubReplyListByCId(cId: Int): Response<List<ReplyResponseModel>> {
        return retrofitClient.service.getClubReplyByCId(cId)
    }

    suspend fun getHotPlaceReplyListByCId(cId: Int): Response<List<ReplyResponseModel>> {
        return retrofitClient.service.getHotPlaceReplyByCId(cId)
    }

    suspend fun getReplyUserByCId(cId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getReplyUserListByCId(cId)
    }

    suspend fun getClubReplyUserByCId(cId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getClubReplyUserListByCId(cId)
    }
    suspend fun getHotPlaceReplyUserByCId(cId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getHotPlaceReplyUserListByCId(cId)
    }

}