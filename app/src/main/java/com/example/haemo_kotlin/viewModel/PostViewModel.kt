package com.example.haemo_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.PostRepository
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
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _postModelList = MutableStateFlow<List<PostResponseModel>>(emptyList())
    val postModelList: StateFlow<List<PostResponseModel>> = _postModelList

    private val _postModel = MutableStateFlow<PostResponseModel?>(null)
    val postModel: StateFlow<PostResponseModel?> = _postModel

    private val _user = MutableStateFlow<UserResponseModel?>(null)
    val user: StateFlow<UserResponseModel?> = _user

    private val _todayPostList = MutableStateFlow<List<PostResponseModel>>(emptyList())
    val todayPostList: StateFlow<List<PostResponseModel>> = _todayPostList

    private val _postModelState = MutableStateFlow<Resource<PostResponseModel>>(Resource.loading(null))
    val postModelState: StateFlow<Resource<PostResponseModel>> = _postModelState.asStateFlow()

    private val _acceptationList = MutableStateFlow<List<AcceptationResponseModel>>(emptyList())
    val acceptationList: StateFlow<List<AcceptationResponseModel>> = _acceptationList

    private val _commentList = MutableStateFlow<List<CommentResponseModel>>(emptyList())
    val commentList: StateFlow<List<CommentResponseModel>> = _commentList


    private val _todayPostModelState =
        MutableStateFlow<Resource<List<PostResponseModel>>>(Resource.loading(null))
    val todayPostModelState: StateFlow<Resource<List<PostResponseModel>>> =
        _todayPostModelState.asStateFlow()


    private val _postModelListState =
        MutableStateFlow<Resource<List<PostResponseModel>>>(Resource.loading(null))
    val postModelListState: StateFlow<Resource<List<PostResponseModel>>> = _postModelListState.asStateFlow()

    suspend fun getPost() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getPost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _postModelList.value = postList!!
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

    suspend fun getTodayPost() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getTodayPost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _todayPostList.value = postList!!
                    _todayPostModelState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _todayPostModelState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _todayPostModelState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getOnePost(idx: Int) {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getOnePost(idx)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _postModel.value = post!!
                    _postModelState.value = Resource.success(post)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                    _postModelState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _postModelState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getPostingUser(pId: Int) {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getPostingUser(pId)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()
                    _user.value = user!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getAcceptationUserByPId(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getJoinUserByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val acceptList = response.body()
                    _acceptationList.value = acceptList!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getCommentListByPId(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCommentListByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val commentList = response.body()
                    _commentList.value = commentList!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }


    fun convertDate(input: String): String {
        val parsedDate = parseDateString(input, "yyyy년 MM월 dd일 hh시")
        val formattedDate = formatDate(parsedDate, "yyyy.MM.dd hh시")

        return formattedDate
    }

    fun parseDateString(dateString: String, format: String): Date {
        val parser = SimpleDateFormat(format, Locale.KOREA)
        return parser.parse(dateString) ?: Date()
    }

    fun formatDate(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.KOREA)
        return formatter.format(date)
    }

}