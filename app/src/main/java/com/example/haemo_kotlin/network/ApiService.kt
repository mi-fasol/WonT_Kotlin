package com.example.haemo_kotlin.network

import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.user.LoginModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.model.user.UserModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("post")
    suspend fun getPost(): Response<List<PostModel>>

    @GET("post/{id}")
    suspend fun getOnePost(@Path("id") idx: Int): Response<PostModel>

    @GET("post/postUser/{id}")
    suspend fun getPostingUser(@Path("id") idx: Int): Response<UserResponseModel>

    @GET("post/24hours")
    suspend fun getTodayPost(): Response<List<PostModel>>

    @POST("/login")
    suspend fun tryLogin(@Body loginModel: LoginModel) : Response<Boolean>

    @POST("/user")
    suspend fun registerUser(@Body user: UserModel) : Response<UserResponseModel>

    @GET("/user/find/{id}")
    suspend fun getUserInfoById(@Path("id") uId: Int) : Response<UserResponseModel>


    @GET("club")
    suspend fun getClubPost(): Response<List<ClubPostModel>>

    @GET("club/{id}")
    suspend fun getClubPostById(@Path("id") idx: Int): Response<ClubPostModel>

    @GET("hot")
    suspend fun getHotPlacePost(): Response<List<HotPlacePostModel>>

    @GET("hot/{id}")
    suspend fun getHotPlacePostById(@Path("id") idx: Int): Response<HotPlacePostModel>

    @GET("hot/popular")
    suspend fun getPopularHotPlacePost(): Response<List<HotPlacePostModel>>
}