package com.example.haemo_kotlin.viewModel.boardInfo

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.wish.WishListModel
import com.example.haemo_kotlin.model.retrofit.wish.WishListResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.WishListRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WishViewModel @Inject constructor(
    private val repository: WishListRepository,
    private val context: Context
) : ViewModel() {

    private val uId = SharedPreferenceUtil(context).getInt("uId", 0)
    var pId = MutableStateFlow(0)

    private val _isDeleted = MutableStateFlow<Boolean>(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    private val _wishMeetingList = MutableStateFlow<List<PostResponseModel>>(emptyList())
    val wishMeetingList: StateFlow<List<PostResponseModel>> = _wishMeetingList

    private val _wishClubList = MutableStateFlow<List<ClubPostResponseModel>>(emptyList())
    val wishClubList: StateFlow<List<ClubPostResponseModel>> = _wishClubList

    private val _wishHotPlaceList = MutableStateFlow<List<HotPlaceResponsePostModel>>(emptyList())
    val wishHotPlaceList: StateFlow<List<HotPlaceResponsePostModel>> = _wishHotPlaceList

    private val _postModelState =
        MutableStateFlow<Resource<PostResponseModel>>(Resource.loading(null))
    val postModelState: StateFlow<Resource<PostResponseModel>> = _postModelState.asStateFlow()

    private val _postModelListState =
        MutableStateFlow<Resource<List<PostResponseModel>>>(Resource.loading(null))
    val postModelListState: StateFlow<Resource<List<PostResponseModel>>> =
        _postModelListState.asStateFlow()

    private val _clubModelListState =
        MutableStateFlow<Resource<List<ClubPostResponseModel>>>(Resource.loading(null))
    val clubModelListState: StateFlow<Resource<List<ClubPostResponseModel>>> =
        _clubModelListState.asStateFlow()

    private val _hotPlaceModelListState =
        MutableStateFlow<Resource<List<HotPlaceResponsePostModel>>>(Resource.loading(null))
    val hotPlaceModelListState: StateFlow<Resource<List<HotPlaceResponsePostModel>>> =
        _hotPlaceModelListState.asStateFlow()

    private val _isWished = MutableStateFlow(false)
    val isWished: StateFlow<Boolean> = _isWished.asStateFlow()

    suspend fun getWishMeeting() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getWishMeetingPost(uId)
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _wishMeetingList.value = postList!!
                    _postModelListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _postModelListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _postModelListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getWishClub() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getWishClubPost(uId)
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _wishClubList.value = postList!!
                    _clubModelListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _clubModelListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _clubModelListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getWishHotPlace() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getWishHotPlacePost(uId)
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _wishHotPlaceList.value = postList!!
                    _hotPlaceModelListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _hotPlaceModelListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _hotPlaceModelListState.value =
                    Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

//    suspend fun checkIsWishedPost(pId: Int, type: Int): Boolean {
//        var result = mutableStateOf(false);
//        viewModelScope.launch {
//            try {
//                val response = repository.checkIsWishedMeetingPost(uId, pId, type)
//                if (response.isSuccessful && response.body() != null) {
//                    val isExist = response.body()
//                    if (isExist != null) {
//                        _isWished.value = isExist
//                        result.value = isExist
//                    }
//                } else {
//                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
//                    Log.e("API Error", "포스트 에러 응답: $errorBody")
//                }
//            } catch (e: Exception) {
//                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
//            }
//        }
//        Log.d("미란링 wishViewModel", result.value.toString())
//        return result.value
//    }

    suspend fun checkIsWishedPost(pId: Int, type: Int): Boolean {
        viewModelScope.launch {
            try {
                val response = repository.checkIsWishedMeetingPost(uId, pId, type)
                if (response.isSuccessful && response.body() != null) {
                    val isExist = response.body()
                    if (isExist != null) {
                        _isWished.value = isExist
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
        Log.d("미란링 wishViewModel", _isWished.value.toString())
        return _isWished.value
    }

    fun addWishList(pId: Int, type: Int) {
        val wish = when (type) {
            1 -> {
                WishListModel(
                    pId,
                    null,
                    null,
                    uId
                )
            }

            2 -> {
                WishListModel(
                    null,
                    pId,
                    null,
                    uId
                )
            }

            else -> {
                WishListModel(
                    null,
                    null,
                    pId,
                    uId
                )
            }
        }

        Log.d("미란 wishClub", wish.toString())
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = when (type) {
                    1 -> repository.addWishPost(wish)
                    2 -> repository.addWishClub(wish)
                    else -> repository.addWishPlace(wish)
                }
                if (response.isSuccessful && response.body() != null) {
                    val wishList = response.body()
                    when (type) {
                        1 -> {
                            _wishMeetingList.value += wishList as PostResponseModel
                        }

                        2 -> {
                            _wishClubList.value += wishList as ClubPostResponseModel
                        }

                        else -> {
                            Log.d("미란 이전 핫플", _wishHotPlaceList.value.toString())
                            _wishHotPlaceList.value += wishList as HotPlaceResponsePostModel
                            Log.d("미란 이후 핫플", _wishHotPlaceList.value.toString())
                        }
                    }
                    _isWished.value = true
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun deleteWishList(pId: Int, type: Int) {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.deleteWishList(uId, pId, type)
                if (response.isSuccessful && response.body() != null) {
                    val deleted = response.body()
                    if (deleted != null) {
                        _isDeleted.value = deleted
                        _isWished.value = !deleted
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }
}