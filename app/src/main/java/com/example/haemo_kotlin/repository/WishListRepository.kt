package com.example.haemo_kotlin.repository

import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.wish.WishListModel
import com.example.haemo_kotlin.model.wish.WishListResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class WishListRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getWishMeetingPost(uId: Int): Response<List<PostResponseModel>> {
        return retrofitClient.service.getWishMeetingPost(uId)
    }

    suspend fun getWishClubPost(uId: Int): Response<List<ClubPostResponseModel>> {
        return retrofitClient.service.getWishClubPost(uId)
    }

    suspend fun getWishHotPlacePost(uId: Int): Response<List<HotPlaceResponsePostModel>> {
        return retrofitClient.service.getWishHotPlacePost(uId)
    }

    suspend fun addWishList(wish: WishListModel, type: Int) : Response<WishListResponseModel>{
        val api = when(type){
            1 -> retrofitClient.service.addWishMeetingPost(wish)
            2 -> retrofitClient.service.addWishClubPost(wish)
            else -> retrofitClient.service.addWishHotPlacePost(wish)
        }
        return api
    }

    suspend fun checkIsWishedMeetingPost(uId: Int, pId: Int, type: Int) : Response<Boolean>{
        val api = when(type){
            1 -> retrofitClient.service.checkIsWishedMeetingPost(uId, pId)
            2 -> retrofitClient.service.checkIsWishedClubPost(uId, pId)
            else -> retrofitClient.service.checkIsWishedHotPlacePost(uId, pId)
        }
        return api
    }

    suspend fun deleteWishList(uId: Int, pId: Int, type: Int) : Response<Boolean> {
        val api = when(type){
            1 -> retrofitClient.service.deleteWishedMeetingPost(uId, pId)
            2 -> retrofitClient.service.deleteWishedClubPost(uId, pId)
            else -> retrofitClient.service.deleteWishedHotPlacePost(uId, pId)
        }
        return api
    }
}