package com.example.haemo_kotlin.viewModel.board

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.post.HotPlacePostModel
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.ImageRepository
import com.example.haemo_kotlin.repository.PostRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class HotPlacePostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val imageRepository: ImageRepository,
    private val context: Context
) : ViewModel() {

    private val _hotPlacePostList = MutableStateFlow<List<HotPlaceResponsePostModel>>(emptyList())
    val hotPlacePostList: StateFlow<List<HotPlaceResponsePostModel>> =
        _hotPlacePostList.asStateFlow()

    private val _popularHotPlace = MutableStateFlow<List<HotPlaceResponsePostModel>>(emptyList())
    val popularHotPlace: StateFlow<List<HotPlaceResponsePostModel>> = _popularHotPlace.asStateFlow()


    private val _hotPlaceModel = MutableStateFlow<HotPlaceResponsePostModel?>(null)
    val hotPlaceModel: StateFlow<HotPlaceResponsePostModel?> = _hotPlaceModel.asStateFlow()

    private val _hotPlacePostState =
        MutableStateFlow<Resource<HotPlaceResponsePostModel>>(Resource.loading(null))
    val hotPlacePostState: StateFlow<Resource<HotPlaceResponsePostModel>> =
        _hotPlacePostState.asStateFlow()

    private val _hotPlacePostListState =
        MutableStateFlow<Resource<List<HotPlaceResponsePostModel>>>(Resource.loading(null))
    val hotPlacePostListState: StateFlow<Resource<List<HotPlaceResponsePostModel>>> =
        _hotPlacePostListState.asStateFlow()

    private val _popularHotPlaceListState =
        MutableStateFlow<Resource<List<HotPlaceResponsePostModel>>>(Resource.loading(null))
    val popularHotPlaceListState: StateFlow<Resource<List<HotPlaceResponsePostModel>>> =
        _popularHotPlaceListState.asStateFlow()

    private val _hotPlacePostRegisterState =
        MutableStateFlow<Resource<HotPlaceResponsePostModel>>(Resource.loading(null))
    val hotPlacePostRegisterState: StateFlow<Resource<HotPlaceResponsePostModel>> =
        _hotPlacePostRegisterState.asStateFlow()


    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val address = MutableStateFlow("정왕")
    val image = MutableStateFlow<List<String>>(emptyList())
    val content = MutableStateFlow("")

    var isValid: StateFlow<Boolean> =
        combine(
            title,
            image,
            description,
            content,
        ) { title, image, description, content ->
            title.isNotBlank() && image.isNotEmpty()&& description.isNotEmpty() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun uploadImageList(imageUri: List<Uri>) {
        viewModelScope.launch {
            try {
                val result = imageRepository.uploadImageList(imageUri)
                image.value = result
                Log.d("사진", result.map { it }.toString())
            } catch (e: HttpException) {
                Log.e("ImageUploadViewModel", "API Error: HTTP ${e.code()} ${e.message}")
            } catch (e: Exception) {
                Log.e("ImageUploadViewModel", "Unknown Error: ${e.message}")
            }
        }
    }

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
                    _hotPlacePostListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _hotPlacePostListState.value =
                    Resource.error(e.message ?: "An error occurred", null)
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
                    _hotPlacePostListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _hotPlacePostListState.value =
                    Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    fun registerPost() {
        val today = getCurrentDateTime()

        val _image = image.value.ifEmpty {
            null
        }

        val post = HotPlacePostModel(
            title.value,
            description.value,
            content.value,
            SharedPreferenceUtil(context).getString("nickname", "")!!.toString(),
            address.value,
            today,
            _image,
            0
        )

        viewModelScope.launch {
            _hotPlacePostRegisterState.value = Resource.loading(null)
            try {
                val response = repository.registerHotPlacePost(post)
                if (response.isSuccessful && response.body() != null) {
//                    _registerState.value = Resource.success(response.body())
                    _hotPlacePostRegisterState.value = Resource.success(response.body())
                    Log.d("게시물 전송", response.body().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }
}