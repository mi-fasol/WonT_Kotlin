package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.retrofit.comment.comment.CommentResponseModel
import com.example.haemo_kotlin.model.retrofit.post.ClubPostModel
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.retrofit.post.HotPlacePostModel
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.retrofit.post.PostModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun registerPost(post: PostModel): Response<PostResponseModel> {
        return retrofitClient.service.registerPost(post)
    }

    suspend fun registerClubPost(post: ClubPostModel): Response<ClubPostResponseModel> {
        return retrofitClient.service.registerClubPost(post)
    }

    suspend fun registerHotPlacePost(post: HotPlacePostModel): Response<HotPlaceResponsePostModel> {
        return retrofitClient.service.registerHotPlacePost(post)
    }

    suspend fun getPost(): Response<List<PostResponseModel>> {
        return retrofitClient.service.getPost()
    }

    suspend fun getTodayPost(): Response<List<PostResponseModel>> {
        return retrofitClient.service.getTodayPost()
    }

    suspend fun getOnePost(idx: Int): Response<PostResponseModel> {
        return retrofitClient.service.getOnePost(idx)
    }

    suspend fun getPostingUser(pId: Int): Response<UserResponseModel> {
        return retrofitClient.service.getPostingUser(pId)
    }


    suspend fun getClubPost(): Response<List<ClubPostResponseModel>> {
        return retrofitClient.service.getClubPost()
    }

    suspend fun getOneClubPost(idx: Int): Response<ClubPostResponseModel> {
        return retrofitClient.service.getClubPostById(idx)
    }

    suspend fun getClubPostingUser(pId: Int): Response<UserResponseModel> {
        return retrofitClient.service.getClubPostingUser(pId)
    }

    suspend fun getHotPlacePost(): Response<List<HotPlaceResponsePostModel>> {
        return retrofitClient.service.getHotPlacePost()
    }

    suspend fun getHotPlaceById(idx: Int): Response<HotPlaceResponsePostModel> {
        return retrofitClient.service.getHotPlacePostById(idx)
    }

    suspend fun getPopularHotPlace(): Response<List<HotPlaceResponsePostModel>> {
        return retrofitClient.service.getPopularHotPlacePost()
    }

    suspend fun getHotPlacePostingUser(pId: Int): Response<UserResponseModel> {
        return retrofitClient.service.getHotPlacePostingUser(pId)
    }

    suspend fun getJoinUserByPId(pId: Int): Response<List<AcceptationResponseModel>> {
        return retrofitClient.service.getJoinUserByPId(pId)
    }

    suspend fun getCommentListByPId(pId: Int): Response<List<CommentResponseModel>> {
        return retrofitClient.service.getCommentListByPId(pId)
    }
}