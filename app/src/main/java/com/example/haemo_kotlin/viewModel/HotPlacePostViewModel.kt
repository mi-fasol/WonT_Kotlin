package com.example.haemo_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HotPlacePostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _hotPlacePostList = MutableStateFlow<List<HotPlacePostModel>>(emptyList())
    val hotPlacePostList: StateFlow<List<HotPlacePostModel>> = _hotPlacePostList.asStateFlow()

    private val _popularHotPlace = MutableStateFlow<List<HotPlacePostModel>>(emptyList())
    val popularHotPlace: StateFlow<List<HotPlacePostModel>> = _popularHotPlace.asStateFlow()


    private val _hotPlaceModel = MutableStateFlow<HotPlacePostModel?>(null)
    val hotPlaceModel: StateFlow<HotPlacePostModel?> = _hotPlaceModel.asStateFlow()

    private val _hotPlacePostState = MutableStateFlow<Resource<HotPlacePostModel>>(Resource.loading(null))
    val hotPlacePostState: StateFlow<Resource<HotPlacePostModel>> = _hotPlacePostState.asStateFlow()

    private val _hotPlacePostListState =
        MutableStateFlow<Resource<List<HotPlacePostModel>>>(Resource.loading(null))
    val hotPlacePostListState: StateFlow<Resource<List<HotPlacePostModel>>> =
        _hotPlacePostListState.asStateFlow()

    private val _popularHotPlaceListState =
        MutableStateFlow<Resource<List<HotPlacePostModel>>>(Resource.loading(null))
    val popularHotPlaceListState: StateFlow<Resource<List<HotPlacePostModel>>> =
        _popularHotPlaceListState.asStateFlow()

    suspend fun getHotPlacePost() {
        viewModelScope.launch {
            _hotPlacePostListState.value = Resource.loading(null)
            try {
                val response = repository.getHotPlacePost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _hotPlacePostList.value = postList!!
                    _hotPlacePostListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _hotPlacePostListState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _hotPlacePostListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getOneHotPlacePost(idx: Int) {
        viewModelScope.launch {
            _hotPlacePostState.value = Resource.loading(null)
            try {
                val response = repository.getHotPlaceById(idx)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _hotPlaceModel.value = post!!
                    _hotPlacePostState.value = Resource.success(post)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                    _hotPlacePostState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _hotPlacePostState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getPopularHotPlace() {
        viewModelScope.launch {
            _hotPlacePostListState.value = Resource.loading(null)
            try {
                val response = repository.getPopularHotPlace()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _popularHotPlace.value = postList!!
                    _hotPlacePostListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _hotPlacePostListState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _hotPlacePostListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }
}