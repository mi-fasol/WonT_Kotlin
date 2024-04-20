package com.example.haemo_kotlin.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.comment.club.ClubCommentResponseModel
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.CommentRepository
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
class CommentViewModel @Inject constructor(
    private val repository: CommentRepository,
    private val context: Context
) : ViewModel() {

    val content = MutableStateFlow("")

    private val _commentList = MutableStateFlow<List<CommentResponseModel>>(emptyList())
    val commentList: StateFlow<List<CommentResponseModel>> = _commentList

    private val _clubCommentList = MutableStateFlow<List<ClubCommentResponseModel>>(emptyList())
    val clubCommentList: StateFlow<List<ClubCommentResponseModel>> = _clubCommentList

    private val _hotPlaceCommentList = MutableStateFlow<List<CommentResponseModel>>(emptyList())
    val hotPlaceCommentList: StateFlow<List<CommentResponseModel>> = _hotPlaceCommentList

    private val _userList = MutableStateFlow<List<UserResponseModel>>(emptyList())
    val userList : StateFlow<List<UserResponseModel>> = _userList

    private val _clubUserList = MutableStateFlow<List<UserResponseModel>>(emptyList())
    val clubUserList : StateFlow<List<UserResponseModel>> = _clubUserList

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

    suspend fun getCommentUser(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCommentUser(pId)
                if (response.isSuccessful && response.body() != null) {
                    val response = response.body()
                    _userList.value = response!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }


    // ClubComment
    suspend fun getClubCommentListByPId(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getClubCommentListByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val commentList = response.body()
                    _clubCommentList.value = commentList!!
                    Log.d("미란: clubCommentList", _clubCommentList.value.size.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getClubCommentUser(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getClubCommentUser(pId)
                if (response.isSuccessful && response.body() != null) {
                    val response = response.body()
                    _clubUserList.value = response!!
                    Log.d("미란: clubUserList", _clubUserList.value.size.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }
}