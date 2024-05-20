package com.example.haemo_kotlin.repository

import android.util.Log
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.wish.WishListModel
import com.example.haemo_kotlin.model.retrofit.wish.WishListResponseModel
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

    suspend fun addWishPost(wish: WishListModel) : Response<PostResponseModel>{
        return retrofitClient.service.addWishMeetingPost(wish)
    }

    suspend fun addWishClub(wish: WishListModel) : Response<ClubPostResponseModel>{
        return retrofitClient.service.addWishClubPost(wish)
    }

    suspend fun addWishPlace(wish: WishListModel) : Response<HotPlaceResponsePostModel>{
        return retrofitClient.service.addWishHotPlacePost(wish)
    }

    suspend fun checkIsWishedMeetingPost(uId: Int, pId: Int, type: Int) : Response<Boolean>{
        val api = when(type){
            1 -> retrofitClient.service.checkIsWishedMeetingPost(uId, pId)
            2 -> retrofitClient.service.checkIsWishedClubPost(uId, pId)
            else -> retrofitClient.service.checkIsWishedHotPlacePost(uId, pId)
        }
        Log.d("미란링 repository pId:", pId.toString())
        Log.d("미란링 repository", api.body().toString())
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