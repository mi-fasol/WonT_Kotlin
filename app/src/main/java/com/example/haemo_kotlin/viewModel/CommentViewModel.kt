package com.example.haemo_kotlin.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.comment.CommentModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.CommentRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: CommentRepository,
    private val context: Context
) : ViewModel() {

    val content = MutableStateFlow("")

    private val _commentList = MutableStateFlow<List<CommentResponseModel>>(emptyList())
    val commentList: StateFlow<List<CommentResponseModel>> = _commentList

    private val _userList = MutableStateFlow<List<UserResponseModel>>(emptyList())
    val userList: StateFlow<List<UserResponseModel>> = _userList

    private val _commentRegisterState =
        MutableStateFlow<Resource<CommentResponseModel>>(Resource.loading(null))
    val commentRegisterState: StateFlow<Resource<CommentResponseModel>> =
        _commentRegisterState.asStateFlow()

    suspend fun getCommentListByPId(pId: Int, type: Int) {
        viewModelScope.launch {
            try {
                val response = when (type) {
                    1 -> repository.getCommentListByPId(pId)
                    2 -> repository.getClubCommentListByPId(pId)
                    3 -> repository.getHotPlaceCommentListByPId(pId)
                    else -> throw (error("오류낫심"))
                }
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

    suspend fun getCommentUser(pId: Int, type: Int) {
        viewModelScope.launch {
            try {
                val response = when (type) {
                    1 -> repository.getCommentUser(pId)
                    2 -> repository.getClubCommentUser(pId)
                    3 -> repository.getHotPlaceCommentUser(pId)
                    else -> throw (error("오류낫심"))
                }
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

    fun registerComment(text: String, pId: Int, type: Int) {
        val nickname = SharedPreferenceUtil(context).getString("nickname", "").toString()

        val comment = when (type) {
            1 -> CommentModel(
                pId,
                null,
                null,
                text,
                nickname,
                getCurrentDateTime()
            )

            2 -> CommentModel(
                null,
                pId,
                null,
                text,
                nickname,
                getCurrentDateTime()
            )

            3 -> CommentModel(
                null,
                null,
                pId,
                text,
                nickname,
                getCurrentDateTime()
            )

            else -> throw (error("에러러링"))
        }

        Log.d("미란 comment", comment.toString())

        viewModelScope.launch {
            _commentRegisterState.value = Resource.loading(null)
            try {
                val response = when (type) {
                    1 -> repository.saveComment(comment)
                    2 -> repository.saveClubComment(comment)
                    3 -> repository.saveHotPlaceComment(comment)
                    else -> throw (error("오류낫심"))
                }
                if (response.isSuccessful && response.body() != null) {
                    val savedComment = response.body()
                    _commentRegisterState.value = Resource.success(savedComment)
                    _commentList.value += savedComment!!
                    Log.d("미란 SaveComment", savedComment.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                    _commentRegisterState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "로그인 요청 중 예외 발생: ${e.message}")
                _commentRegisterState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }
}