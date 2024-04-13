package com.example.haemo_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.PostRepository
import com.example.haemo_kotlin.repository.WishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class WishViewModel @Inject constructor(
    private val repository: WishRepository
) : ViewModel() {

    private val _wishMeetingList = MutableStateFlow<List<PostResponseModel>>(emptyList())
    val wishMeetingList: StateFlow<List<PostResponseModel>> = _wishMeetingList

    private val _wishClubList = MutableStateFlow<List<ClubPostResponseModel>>(emptyList())
    val wishClubList: StateFlow<List<ClubPostResponseModel>> = _wishClubList

    private val _wishHotPlaceList = MutableStateFlow<List<HotPlaceResponsePostModel>>(emptyList())
    val wishHotPlaceList: StateFlow<List<HotPlaceResponsePostModel>> = _wishHotPlaceList

    private val _postModelState = MutableStateFlow<Resource<PostResponseModel>>(Resource.loading(null))
    val postModelState: StateFlow<Resource<PostResponseModel>> = _postModelState.asStateFlow()

    private val _postModelListState =
        MutableStateFlow<Resource<List<PostResponseModel>>>(Resource.loading(null))
    val postModelListState: StateFlow<Resource<List<PostResponseModel>>> = _postModelListState.asStateFlow()

    private val _clubModelListState =
        MutableStateFlow<Resource<List<ClubPostResponseModel>>>(Resource.loading(null))
    val clubModelListState: StateFlow<Resource<List<ClubPostResponseModel>>> = _clubModelListState.asStateFlow()

    private val _hotPlaceModelListState =
        MutableStateFlow<Resource<List<HotPlaceResponsePostModel>>>(Resource.loading(null))
    val hotPlaceModelListState: StateFlow<Resource<List<HotPlaceResponsePostModel>>> = _hotPlaceModelListState.asStateFlow()

    suspend fun getWishMeeting(uId: Int) {
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

    suspend fun getWishClub(uId: Int) {
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

    suspend fun getWishHotPlace(uId: Int) {
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
                _hotPlaceModelListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }
}